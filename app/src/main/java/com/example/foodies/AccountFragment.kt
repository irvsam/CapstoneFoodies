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


class AccountFragment : Fragment() {
    private lateinit var accountViewModel: AccountViewModel // Declare the UserViewModel
    private lateinit var vendorManagementViewModel: VendorManagementViewModel
    private var user: Entities.User? = null // Declare the User property as nullable
    private var vendor: Entities.User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSignOutButton()
        setUserDetails()

    }

    private fun setSignOutButton() {
        //setting the logic for signing out
        val signOutButton = view?.findViewById<Button>(R.id.signout_button)

        signOutButton?.setOnClickListener {
            // Perform the sign-out logic
            // sharedViewModel.loggedIn = false

            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)

        }
    }

    private fun setUserDetails() {
        // Initialize the UserViewModel
        accountViewModel = ViewModelProvider(requireActivity())[AccountViewModel::class.java]
        vendorManagementViewModel =
            ViewModelProvider(requireActivity())[VendorManagementViewModel::class.java]
        // Get the user from the UserViewModel
        user = accountViewModel.user

        //this is the logged on user
        vendor = vendorManagementViewModel.user

        // Check if the user is not null before accessing its properties
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

            if (vendor?.type == "Vendor") { // user is a vendor
                avatar?.visibility = View.GONE
                rewardRowTitle?.visibility = View.GONE
                rewardTextView?.visibility = View.GONE
                voucherTextView?.visibility = View.GONE
                voucherRowTitle?.visibility = View.GONE
                //editButton?.visibility = View.GONE
                nameTextView?.text = vendor?.username
                emailTextView?.text = vendor?.email
                phoneTextView?.text = vendor?.phone
                descriptionTextView?.text = vendorManagementViewModel.vendor?.description


            } else { // user is not a vendor
                nameTextView?.text = user?.username
                emailTextView?.text = user?.email
                phoneTextView?.text = user?.phone

                if (user?.avatar != null) {
                    setAvatarImage(user?.avatar, avatar)
                }
                descriptionRowTitle?.visibility = View.GONE
                descriptionTextView?.visibility = View.GONE

                // Observe changes to the active voucher code
                accountViewModel.userVoucher.observe(viewLifecycleOwner) { voucherCode ->
                    if (voucherTextView != null) {
                        if (voucherCode != null) {
                            voucherTextView.text = voucherCode
                        }
                    }
                }
                //load initial voucher
                val userId = accountViewModel.user?.id
                if (userId != null) {
                    accountViewModel.loadUserInitialVoucher(userId)
                }
                //observe changes to reward points
                accountViewModel.userTotalPoints.observe(viewLifecycleOwner) { totalPoints ->
                    if (rewardTextView != null) {
                        rewardTextView.text = totalPoints.toString()
                    }
                }

                // Load the user's initial reward points from the database
                if (userId != null) {
                    accountViewModel.loadUserInitialRewardPoints(userId)
                }
                avatar?.setOnClickListener {
                    // Open the avatar selection dialog when the current avatar is clicked
                    showAvatarSelectionDialog()
                }

            }

            editButton?.setOnClickListener {
                //edit account details (only username and phone number)
                val navController = findNavController()
                navController.navigate(R.id.editDetailsFragment)

            }
        }

    }

    // Define a map to map avatar names to drawable resources
    private val avatarMap = mapOf(
        "penguin" to R.drawable.penguin,
        "rabbit" to R.drawable.rabbit,
        "sloth" to R.drawable.sloth,
        "camel" to R.drawable.camel,
        "secret" to R.drawable.secret
    )

    // Function to set the avatar image based on the avatar name
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
