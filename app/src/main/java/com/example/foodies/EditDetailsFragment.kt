package com.example.foodies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import classes.Entities
import classes.AccountViewModel

class EditDetailsFragment : Fragment() {
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var usernameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var saveButton: Button
    private var user: Entities.User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountViewModel = ViewModelProvider(requireActivity())[AccountViewModel::class.java]

        usernameEditText = view.findViewById(R.id.edit_username)
        phoneEditText = view.findViewById(R.id.edit_phone)
        saveButton = view.findViewById(R.id.save_button)

        // Get the user from the UserViewModel
        user = accountViewModel.user

        // Set the initial values for the username and phone EditText fields
        usernameEditText.setText(user?.username)
        phoneEditText.setText(user?.phone)

        saveButton.setOnClickListener {
            // Update the user's details with the values in the EditText fields
            val newUsername = usernameEditText.text.toString()
            val newPhone = phoneEditText.text.toString()

            if (user != null) {
                // Update the user object
                user!!.username = newUsername
                user!!.phone = newPhone

                //UserViewModel to update the user's details in the database
                accountViewModel.updateUserDetails(user!!)

                // Navigate back
                val navController = findNavController()
                navController.navigate(R.id.accountFragment)
            }
        }
    }
}
