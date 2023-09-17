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

class RewardsFragment: Fragment()  {

    private lateinit var accountViewModel: AccountViewModel
    private lateinit var autoCompleteUserTextView: AutoCompleteTextView
    private lateinit var adapterItems: ArrayAdapter<String>
    private val vendorRepository: VendorRepository = VendorRepository()
    // Declare the notificationManager variable
    private lateinit var notificationManager: NotificationManagerCompat




    private lateinit var rewardAmtTextView: TextView // Reference to the earned points TextView
    private lateinit var vendorSpinner: Spinner // Reference to the vendor selection Spinner
    private lateinit var qrButton: Button // Button to open QR code scanner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val view = inflater.inflate(R.layout.fragment_rewards, container, false)

        rewardAmtTextView = view.findViewById(R.id.rewardAmt)
        // Load the user's points and update the UI
        accountViewModel = ViewModelProvider(requireActivity())[AccountViewModel::class.java]


        //get the list of vendors and load it into the auto complete text thing
        autoCompleteUserTextView = view.findViewById(R.id.vendor_input)
        val vendorList = mutableListOf<String>()
        adapterItems = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, vendorList)
        autoCompleteUserTextView.setAdapter(adapterItems)
        autoCompleteUserTextView.threshold = 1

        // Launch a coroutine to fetch the vendor list
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val fetchedVendorList = vendorRepository.getVendorList()
                vendorList.clear()
                vendorList.addAll(fetchedVendorList)
                adapterItems.notifyDataSetChanged()
            } catch (e: Exception) {
                // Handle any exceptions that may occur during the data fetch
                Log.e(TAG, "Error fetching vendor list: ${e.message}", e)
            }
        }

        // Handle item selection from the AutoCompleteTextView (as shown in the previous response)
        autoCompleteUserTextView.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position) as String

        }


        // Observe the LiveData for reward points
        accountViewModel.userRewardPoints.observe(viewLifecycleOwner) { rewardPoints ->
            rewardAmtTextView.text = rewardPoints.toString()
            val claimButton = view.findViewById<Button>(R.id.claimButton)
            val goodNewsText = view.findViewById<TextView>(R.id.goodNewsText)
            val autoCompleteTextView = view.findViewById<AutoCompleteTextView>(R.id.vendor_input)
            val vendorTypeLayout = view.findViewById<TextInputLayout>(R.id.vendor_type_layout)


            if (rewardPoints < 50) {
                // If reward points are below 50, update UI
                goodNewsText.text = "Earn 50 points to claim a voucher"
                claimButton.isEnabled = false
                claimButton.setBackgroundColor(resources.getColor(R.color.grey)) // Set the background color to grey
                autoCompleteTextView.visibility = View.INVISIBLE
                vendorTypeLayout.visibility = View.INVISIBLE

                // Move the claimButton upwards by setting its top constraint to the placeholder view
                val params = claimButton.layoutParams as ConstraintLayout.LayoutParams
                params.topToBottom = R.id.goodNewsText
                claimButton.layoutParams = params
            } else {
                // If reward points are 50 or more, update UI
                goodNewsText.text = "Good news! You have enough points for a voucher"
                claimButton.isEnabled = true
                claimButton.setBackgroundColor(resources.getColor(R.color.nicegreen))
                autoCompleteTextView.visibility = View.VISIBLE
                vendorTypeLayout.visibility = View.VISIBLE

                // Reset the claimButton's top constraint to the goodNewsText
                val params = claimButton.layoutParams as ConstraintLayout.LayoutParams
                params.topToBottom = R.id.vendor_type_layout
                claimButton.layoutParams = params
            }
        }

        // Load the user's initial reward points from the database
        val userId = accountViewModel.user?.id // Replace with the actual user ID
        if (userId != null) {
            accountViewModel.loadUserInitialRewardPoints(userId)

        }


        val claimButton = view.findViewById<Button>(R.id.claimButton)
        claimButton.setOnClickListener {
            val selectedVendor = autoCompleteUserTextView.text.toString()
            if (selectedVendor.isEmpty()) {
                Toast.makeText(requireContext(), "Choose a vendor first", Toast.LENGTH_SHORT).show()
            } else {
                // Show a notification when the button is clicked
                //create a voucher code  that has the first three letters of the selectedVendor followed by 6 random digits
                val voucherCode = generateVoucherCode(selectedVendor)
                Log.d(TAG, "created voucher $voucherCode.")
                Toast.makeText(requireContext(), "Voucher has been sent to your e-mail and added to your account", Toast.LENGTH_SHORT).show()
                resetPoints()
                updateVoucher(voucherCode)
            }
        }

        return view


    }

    private fun updateVoucher(v: String){
        val userId = accountViewModel.user?.id // Replace with the actual user ID
        if (userId != null) {
            accountViewModel.updateUserVoucher(userId,v)
            Log.d(TAG, "updating voucher to $v")
        }

    }
    private fun resetPoints() {
        val userId = accountViewModel.user?.id // Replace with the actual user ID
        if (userId != null) {
            accountViewModel.resetUserRewardPoints(userId)
        }
    }

    private fun generateVoucherCode(vendorName: String): String {
        // Extract the first two letters from the vendor name
        val firstThreeLetters = vendorName.substring(0, 3)
        // Generate 6 random digits
        val randomDigits = (100_000..999_999).random()
        // Combine the first two letters and random digits to create the voucher code
        return "$firstThreeLetters$randomDigits"
    }


}

