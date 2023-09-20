package com.example.foodies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import classes.Entities
import classes.AccountViewModel
import classes.VendorManagementViewModel

/** this is the account home page where accpunt details can be viewed and edited*/
class AccountFragment : Fragment() {
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var vendorManagementViewModel: VendorManagementViewModel
    private var user: Entities.User? = null
    private var vendor: Entities.User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSignOutButton()
        setUserDetails()
    }

    /** method to set up the action of the sign out button*/
    //TODO log out all possibilities or set them to null??
    private fun setSignOutButton() {
        val signOutButton = view?.findViewById<Button>(R.id.signout_button)

        signOutButton?.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }

    /** set the UI to the users details*/
    private fun setUserDetails() {
        accountViewModel = ViewModelProvider(requireActivity())[AccountViewModel::class.java]
        vendorManagementViewModel = ViewModelProvider(requireActivity())[VendorManagementViewModel::class.java]

        /** if a general user is logged in*/
        user = accountViewModel.user
        /** if a vendor account is logged in*/
        vendor = vendorManagementViewModel.user


        if (user != null || vendor != null) {
            val nameTextView = view?.findViewById<TextView>(R.id.nameTextView)
            val emailTextView = view?.findViewById<TextView>(R.id.emailTextView)
            val phoneTextView = view?.findViewById<TextView>(R.id.phoneTextView)
            val rewardTextView = view?.findViewById<TextView>(R.id.rewardTextView)
            val rewardRowTitle = view?.findViewById<TextView>(R.id.rewardRowTitle)
            val voucherTextView = view?.findViewById<TextView>(R.id.voucherTextView)
            val voucherRowTitle = view?.findViewById<TextView>(R.id.voucherRowTitle)
            val editButton = view?.findViewById<ImageButton>(R.id.edit_button)
            val descriptionRowTitle = view?.findViewById<TextView>(R.id.descriptionRowTitle)
            val descriptionTextView = view?.findViewById<TextView>(R.id.descriptionTextView)
            val avatar = view?.findViewById<ImageView>(R.id.avatarImageView)

            /** first check if a vendor account has been logged in*/
            if (vendor?.type == "Vendor") {
                avatar?.visibility = View.GONE
                rewardRowTitle?.visibility = View.GONE
                rewardTextView?.visibility = View.GONE
                voucherTextView?.visibility = View.GONE
                voucherRowTitle?.visibility = View.GONE
                nameTextView?.text = vendor?.username
                emailTextView?.text = vendor?.email
                phoneTextView?.text = vendor?.phone
                descriptionTextView?.text = vendorManagementViewModel.vendor?.description

                /** otherwise it is a general user */
            } else {
                nameTextView?.text = user?.username
                emailTextView?.text = user?.email
                phoneTextView?.text = user?.phone

                if (user?.avatar != null) {
                    setAvatarImage(user?.avatar, avatar)
                }
                descriptionRowTitle?.visibility = View.GONE
                descriptionTextView?.visibility = View.GONE
                /** observe live data for voucher codes */
                accountViewModel.userVoucher.observe(viewLifecycleOwner) { voucherCode ->
                    if (voucherTextView != null) {
                        if (voucherCode != null) {
                            voucherTextView.text = voucherCode
                        }
                    }
                }
                val userId = accountViewModel.user?.id
                if (userId != null) {
                    accountViewModel.loadUserInitialVoucher(userId)
                }
                /** observe live data for reward points*/
                accountViewModel.userTotalPoints.observe(viewLifecycleOwner) { totalPoints ->
                    if (rewardTextView != null) {
                        rewardTextView.text = totalPoints.toString()
                    }
                }
                if (userId != null) {
                    accountViewModel.loadUserInitialRewardPoints(userId)
                }

                /** edit avatar */
                avatar?.setOnClickListener {
                    showAvatarSelectionDialog()
                }

            }

            /** edit account details */
            editButton?.setOnClickListener {
                val navController = findNavController()
                navController.navigate(R.id.editDetailsFragment)

            }
        }

    }

    /** some avatar setting logic*/

    private val avatarMap = mapOf(
        "penguin" to R.drawable.penguin,
        "rabbit" to R.drawable.rabbit,
        "sloth" to R.drawable.sloth,
        "camel" to R.drawable.camel,
        "secret" to R.drawable.secret
    )

    private fun setAvatarImage(avatarName: String?, avatarImageView: ImageView?) {
        avatarName?.let {
            val resourceId = avatarMap[it]
            if (resourceId != null) {
                avatarImageView?.setImageResource(resourceId)
            }
        }
    }

    private fun showAvatarSelectionDialog() {
        val dialogFragment = AvatarSelectionDialogFragment()
        dialogFragment.setOnAvatarSelectedListener(object :
            AvatarSelectionDialogFragment.OnAvatarSelectedListener {
            override fun onAvatarSelected(avatarResId: Int) {
                // Handle the selected avatar here
                updateAvatarInDatabase(avatarResId)
            }
        })
        dialogFragment.show(childFragmentManager, "AvatarSelectionDialog")
    }

    private fun updateAvatarInDatabase(avatarResId: Int) {
        val newAvatarName = avatarMap.entries.firstOrNull { it.value == avatarResId }?.key
        user?.avatar = newAvatarName
        user?.let { accountViewModel.updateUserDetails(it) }

        val avatarImageView = view?.findViewById<ImageView>(R.id.avatarImageView)
        avatarImageView?.setImageResource(avatarResId)
    }

}
