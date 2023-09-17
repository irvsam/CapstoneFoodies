package com.example.foodies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import classes.Entities
import classes.AccountViewModel
import classes.StoreViewModel
import classes.VendorManagementViewModel


class AccountFragment : Fragment() {
    private lateinit var accountViewModel: AccountViewModel // Declare the UserViewModel
    private lateinit var vendorManagementViewModel: VendorManagementViewModel
    private var user: Entities.User? = null // Declare the User property as nullable
    private var vendor: Entities.User?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUserDetails()
        setSignOutButton()

    }


    private fun setSignOutButton(){
            //setting the logic for signing out
        val signOutButton = view?.findViewById<Button>(R.id.signout_button)

        signOutButton?.setOnClickListener {
            // Perform the sign-out logic
            // sharedViewModel.loggedIn = false

            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)

        }
    }

    private fun setUserDetails(){
        // Initialize the UserViewModel
        accountViewModel = ViewModelProvider(requireActivity())[AccountViewModel::class.java]
        vendorManagementViewModel = ViewModelProvider(requireActivity())[VendorManagementViewModel::class.java]
        // Get the user from the UserViewModel
        user = accountViewModel.user

        //this is the logged on user
        vendor = vendorManagementViewModel.user

        // Check if the user is not null before accessing its properties
        if (user != null || vendor!=null) {
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

            if(vendor?.type=="Vendor"){ // user is a vendor

                rewardRowTitle?.visibility=View.GONE
                rewardTextView?.visibility=View.GONE
                voucherTextView?.visibility = View.GONE
                voucherRowTitle?.visibility = View.GONE
                //editButton?.visibility = View.GONE
                nameTextView?.text = vendor?.username
                emailTextView?.text = vendor?.email
                phoneTextView?.text = vendor?.phone
                descriptionTextView?.text = vendorManagementViewModel.vendor?.description


            }
            else { // user is not a vendor
                nameTextView?.text = user?.username
                emailTextView?.text = user?.email
                phoneTextView?.text = user?.phone
                descriptionRowTitle?.visibility = View.GONE
                descriptionTextView?.visibility = View.GONE

                // Observe changes to the active voucher code
                accountViewModel.userVoucher.observe(viewLifecycleOwner) { voucherCode ->
                    if (voucherTextView != null) {
                        if(voucherCode!=null)
                        {voucherTextView.text = voucherCode}
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


            }
            editButton?.setOnClickListener {
                //edit account details (only username and phone number)
                val navController = findNavController()
                navController.navigate(R.id.editDetailsFragment)


            }







        }


    }
}
