package com.example.foodies

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import classes.AccountViewModel
import classes.VendorRepository
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

/** this is the rewards page where a user sees their current rewards
 * and can claim a voucher if their rewards are high enough */
class RewardsFragment: Fragment()  {

    private lateinit var accountViewModel: AccountViewModel
    private lateinit var autoCompleteUserTextView: AutoCompleteTextView
    private lateinit var adapterItems: ArrayAdapter<String>
    private val vendorRepository: VendorRepository = VendorRepository()
    private lateinit var rewardAmtTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val view = inflater.inflate(R.layout.fragment_rewards, container, false)
        rewardAmtTextView = view.findViewById(R.id.rewardAmt)
        accountViewModel = ViewModelProvider(requireActivity())[AccountViewModel::class.java]
        autoCompleteUserTextView = view.findViewById(R.id.vendor_input)
        val vendorList = mutableListOf<String>()
        adapterItems = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, vendorList)
        autoCompleteUserTextView.setAdapter(adapterItems)
        autoCompleteUserTextView.threshold = 1

        /** fetch the vendor list for the dropdown menu*/
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val fetchedVendorList = vendorRepository.getVendorList()
                vendorList.clear()
                vendorList.addAll(fetchedVendorList)
                adapterItems.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching vendor list: ${e.message}", e)
            }
        }
        /** vendor selection */
        autoCompleteUserTextView.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position) as String
        }

        /** observe the live user rewards */
            accountViewModel.userRewardPoints.observe(viewLifecycleOwner) { rewardPoints ->
            rewardAmtTextView.text = rewardPoints.toString()
            val claimButton = view.findViewById<Button>(R.id.claimButton)
            val goodNewsText = view.findViewById<TextView>(R.id.goodNewsText)
            val autoCompleteTextView = view.findViewById<AutoCompleteTextView>(R.id.vendor_input)
            val vendorTypeLayout = view.findViewById<TextInputLayout>(R.id.vendor_type_layout)

                /** user does not have enough points yet  update UI accordingly */
            if (rewardPoints < 50) {
                goodNewsText.text = "Earn 50 points to claim a voucher"
                claimButton.isEnabled = false
                claimButton.setBackgroundColor(resources.getColor(R.color.grey))
                autoCompleteTextView.visibility = View.INVISIBLE
                vendorTypeLayout.visibility = View.INVISIBLE
                val params = claimButton.layoutParams as ConstraintLayout.LayoutParams
                params.topToBottom = R.id.goodNewsText
                claimButton.layoutParams = params
            } else {
                /** user has enough points for a voucher, update UI */
                goodNewsText.text = "Good news! You have enough points for a voucher"
                claimButton.isEnabled = true
                claimButton.setBackgroundColor(resources.getColor(R.color.nicegreen))
                autoCompleteTextView.visibility = View.VISIBLE
                vendorTypeLayout.visibility = View.VISIBLE
                val params = claimButton.layoutParams as ConstraintLayout.LayoutParams
                params.topToBottom = R.id.vendor_type_layout
                claimButton.layoutParams = params
            }
        }
        /** load initial points from db */
        val userId = accountViewModel.user?.id
        if (userId != null) {
            accountViewModel.loadUserInitialRewardPoints(userId)

        }
        /** user selects claim so give them a voucher */
        val claimButton = view.findViewById<Button>(R.id.claimButton)
        claimButton.setOnClickListener {
            val selectedVendor = autoCompleteUserTextView.text.toString()
            if (selectedVendor.isEmpty()) {
                Toast.makeText(requireContext(), "Choose a vendor first", Toast.LENGTH_SHORT).show()
            } else {
                val voucherCode = generateVoucherCode(selectedVendor)
                Toast.makeText(requireContext(), "Voucher has been sent to your e-mail and added to your account", Toast.LENGTH_SHORT).show()
                resetPoints()
                updateVoucher(voucherCode)
            }
        }

        return view
    }

    private fun updateVoucher(v: String){
        val userId = accountViewModel.user?.id
        if (userId != null) {
            accountViewModel.updateUserVoucher(userId,v)
            Log.d(TAG, "updating voucher to $v")
        }

    }
    private fun resetPoints() {
        val userId = accountViewModel.user?.id
        if (userId != null) {
            accountViewModel.resetUserRewardPoints(userId)
        }
    }

    /** generate a voucher code based on the vendor name */
    private fun generateVoucherCode(vendorName: String): String {
        val firstThreeLetters = vendorName.substring(0, 3)
        val randomDigits = (100_000..999_999).random()
        return "$firstThreeLetters$randomDigits"
    }


}

