package com.example.foodies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import classes.Entities
import classes.AccountViewModel
import classes.VendorManagementViewModel

/** editing account details*/
class EditDetailsFragment : Fragment() {
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var vendorManagementViewModel: VendorManagementViewModel
    private lateinit var usernameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var saveButton: Button
    private var user: Entities.User? = null
    private var vendor: Entities.Vendor? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountViewModel = ViewModelProvider(requireActivity())[AccountViewModel::class.java]
        vendorManagementViewModel = ViewModelProvider(requireActivity())[VendorManagementViewModel::class.java]
        usernameEditText = view.findViewById(R.id.edit_username)
        phoneEditText = view.findViewById(R.id.edit_phone)
        descriptionEditText = view.findViewById(R.id.edit_description)
        saveButton = view.findViewById(R.id.save_button)

        // if the current user is a vendor
        if(vendorManagementViewModel.isVendor){
            user = vendorManagementViewModel.user
            vendor = vendorManagementViewModel.vendor
            usernameEditText.setText(user?.username)
            phoneEditText.setText(user?.phone)
            descriptionEditText.setText(vendor?.description)

            saveButton.setOnClickListener {
                val newUsername = usernameEditText.text.toString()
                val newPhone = phoneEditText.text.toString()
                val newDescription = descriptionEditText.text.toString()
                if (newDescription.isNotEmpty() && newPhone.isNotEmpty() && newUsername.isNotEmpty()) {

                    if (user != null) {
                        //set new details and update
                        user!!.username = newUsername
                        user!!.phone = newPhone
                        vendor!!.description = newDescription
                        vendorManagementViewModel.updateUserDetails(user!!)
                        vendorManagementViewModel.updateVendorDetails(vendor!!)
                        // Navigate back when done
                        val navController = findNavController()
                        navController.navigate(R.id.accountFragment)
                    }
                }
                else{Toast.makeText(requireContext(),"details can't be empty",Toast.LENGTH_SHORT).show()}
            }


        }
        else{ // User is not a vendor account
            user = accountViewModel.user
            descriptionEditText.visibility = View.GONE
            usernameEditText.setText(user?.username)
            phoneEditText.setText(user?.phone)

            saveButton.setOnClickListener {
                val newUsername = usernameEditText.text.toString()
                val newPhone = phoneEditText.text.toString()

                if (user != null) {
                    if (newPhone.isNotEmpty() && newUsername.isNotEmpty()) {
                        //Set the new details and update
                        user!!.username = newUsername
                        user!!.phone = newPhone
                        accountViewModel.updateUserDetails(user!!)

                        // Navigate back when done
                        val navController = findNavController()
                        navController.navigate(R.id.accountFragment)
                    }
                    else{Toast.makeText(requireContext(),"details can't be empty",Toast.LENGTH_SHORT).show()}
                }
                }
            }
    }
}
