package com.example.foodies

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import classes.Entities
import classes.GuestViewModel
import classes.SharedViewModel
import classes.UserViewModel
import classes.VendorViewModel
import org.w3c.dom.Text


class AccountFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel // Declare the UserViewModel
    private lateinit var vendorViewModel: VendorViewModel
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
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        vendorViewModel = ViewModelProvider(requireActivity())[VendorViewModel::class.java]
        // Get the user from the UserViewModel
        user = userViewModel.user
        vendor = vendorViewModel.user

        // Check if the user is not null before accessing its properties
        if (user != null || vendor!=null) {
            val nameTextView = view?.findViewById<TextView>(R.id.nameTextView)
            val emailTextView = view?.findViewById<TextView>(R.id.emailTextView)
            val phoneTextView = view?.findViewById<TextView>(R.id.phoneTextView)
            val rewardTextView = view?.findViewById<TextView>(R.id.rewardTextView)
            val rewardRowTitle = view?.findViewById<TextView>(R.id.rewardRowTitle)
            val voucherTextView = view?.findViewById<TextView>(R.id.voucherTextView)

            if(vendor?.type=="Vendor"){
                rewardRowTitle?.visibility=View.GONE
                rewardTextView?.visibility=View.GONE
                nameTextView?.text = vendor?.username
                emailTextView?.text = vendor?.email
                phoneTextView?.text = vendor?.phone
            }
            else {
                nameTextView?.text = user?.username
                emailTextView?.text = user?.email
                phoneTextView?.text = user?.phone
                // Observe changes to the active voucher code
                userViewModel.userVoucher.observe(viewLifecycleOwner) { voucherCode ->
                    if (voucherTextView != null) {
                        if(voucherCode!=null)
                        {voucherTextView.text = voucherCode}
                    }
                }
                //load initial voucher
                val userId = userViewModel.user?.id
                if (userId != null) {
                    userViewModel.loadUserInitialVoucher(userId)
                }


                userViewModel.userTotalPoints.observe(viewLifecycleOwner) { totalPoints ->
                    if (rewardTextView != null) {
                        rewardTextView.text = totalPoints.toString()
                    }
                }

                // Load the user's initial reward points from the database
                if (userId != null) {
                    userViewModel.loadUserInitialRewardPoints(userId)
                }
            }


        }


    }
}
