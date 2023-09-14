package com.example.foodies

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import classes.Entities
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity: AppCompatActivity() {

    private val userTypes = listOf("Student","Staff Member","Vendor")
    private lateinit var autoCompleteUserTextView: AutoCompleteTextView
    private lateinit var adapterItems:ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // calling the action bar
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)


        // Find references to the EditText fields and registration button
        val usernameEditText = findViewById<EditText>(R.id.username_input)
        val emailEditText = findViewById<EditText>(R.id.email_input)
        val phoneEditText = findViewById<EditText>(R.id.phone_input)
        val passwordEditText = findViewById<EditText>(R.id.password_input)
        val passwordRepeatEditText = findViewById<EditText>(R.id.password_repeat_input)
        val registerButton = findViewById<Button>(R.id.register_btn)
        val vendorIdLayout = findViewById<TextInputLayout>(R.id.vendorID_layout)
        val vendorIdText = findViewById<EditText>(R.id.vendorID_input)
        vendorIdLayout.visibility = View.GONE

        autoCompleteUserTextView = findViewById<AutoCompleteTextView>(R.id.user_type_input)
        adapterItems = ArrayAdapter<String>(this,R.layout.list_user_types,userTypes) // Use userTypes as the various clickable options in drop down menu
        autoCompleteUserTextView.setAdapter(adapterItems)
        autoCompleteUserTextView.onItemClickListener = AdapterView.OnItemClickListener{
            adapterView,view,i,l->

            val itemSelected = adapterView.getItemAtPosition(i)
            Toast.makeText(this,"Account type: $itemSelected",Toast.LENGTH_SHORT).show()
            if (itemSelected=="Vendor"){ // If user is a vendor they must give an associated vendorID
                Log.d("IF clause","entered")
                vendorIdLayout.visibility=View.VISIBLE
            }
            else{  // If user is not a vendor then Vendor ID must not be displayed to them
                vendorIdLayout.visibility=View.GONE
                //vendorIdText.setText("-1L")
            }
        }

        registerButton.setOnClickListener {
            // retrieve the input from the EditText fields
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val password = passwordEditText.text.toString()
            val passwordRepeat = passwordRepeatEditText.text.toString()
            val userType = autoCompleteUserTextView.text.toString()
            var vendorID:Long = -1
            if(userType=="Vendor") {
                vendorID = (vendorIdText.text.toString()).toLong()
            }


            //TODO: validate passwords, username, and email (no duplicates, passwords match, etc)


            //Insert the user data into the Room database using the DAO
            val user = Entities.User(username = username,
                email = email,
                phone = phone,
                password = password,
                type = userType,
                vendorId = vendorID,
                rewardPoints = 0)

            if (validateRegistrationInput(username, email, phone, password, passwordRepeat)) {
                // registration is successful
                // insert user on a thread other than the main thread
                lifecycleScope.launch {
                    insertUserInBackground(user)
                }

                val toast = Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT)
                toast.show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

    }

    private fun validateRegistrationInput(
        username: String,
        email: String,
        phone: String,
        password: String,
        passwordRepeat: String
    ): Boolean {
        // TODO: Perform your validation checks here (username/email/phone already exists, passwords don't match)
        if (username.isEmpty()
            || email.isEmpty()
            || phone.isEmpty()
            || password.isEmpty()
            || passwordRepeat.isEmpty()) {
            showToast("All fields are required")
            return false
        }

        if (!isValidEmail(email)) {
            showToast("Invalid email format")
            return false
        }

        if (password != passwordRepeat) {
            showToast("Passwords do not match")
            return false
        }

        return true
    }

    private fun isValidEmail(email: String): Boolean {
        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private suspend fun insertUserInBackground(user: Entities.User) {
        withContext(Dispatchers.IO) {
            ApplicationCore.database.accountDao().insertUser(user)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Handle the back navigation to the login page
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

