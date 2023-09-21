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
import classes.daos.AccountDao
import com.example.foodies.databaseManagement.ApplicationCore
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class RegisterActivity: AppCompatActivity() {

    private val userTypes = listOf("Student","Staff Member","Vendor")
    private lateinit var autoCompleteUserTextView: AutoCompleteTextView
    private lateinit var adapterItems:ArrayAdapter<String>
    private val accountDao: AccountDao = ApplicationCore.database.accountDao()


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

        autoCompleteUserTextView = findViewById(R.id.user_type_input)
        adapterItems = ArrayAdapter<String>(this,R.layout.list_user_types,userTypes) // Use userTypes as the various clickable options in drop down menu
        autoCompleteUserTextView.setAdapter(adapterItems)
        autoCompleteUserTextView.onItemClickListener = AdapterView.OnItemClickListener{
            adapterView,_,i,_->

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

            //Insert the user data into the Room database using the DAO
            val user = Entities.User(username = username,
                email = email,
                phone = phone,
                password = password,
                type = userType,
                vendorId = vendorID,
                rewardPoints = 0,
                totalOverAllPoints = 0)

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

    // Method to ensure the information entered in the registration fields is valid
    // (Confirmed passwords; in-use usernames/emails/phone)
    private fun validateRegistrationInput(
        username: String,
        email: String,
        phone: String,
        password: String,
        passwordRepeat: String
    ): Boolean {
        if (username.isEmpty()
            || email.isEmpty()
            || phone.isEmpty()
            || password.isEmpty()
            || passwordRepeat.isEmpty()) {
            showToast("All fields are required")
            return false
        }

        var duplicateField = false

        // Make sure main thread waits for isDuplicateEntry function to operate on a new thread
    runBlocking {
        val job = GlobalScope.launch {
            duplicateField = isDuplicateEntry(email, phone, username, accountDao)
        }
        job.join()
    }

        // if one of the entered fields contains a duplicate entry to another account
        if (duplicateField) {
            showToast("One or more of the fields you have entered is already in use!")
            return false
        }
        // If the email is not valid
        if (!isValidEmail(email)) {
            showToast("Enter a valid UCT e-mail")
            return false
        }
        // If the passwords entered do not match
        if (password != passwordRepeat) {
            showToast("Passwords do not match")
            return false
        }

        return true
    }
    // Checks the emails entered are UCT-affiliated emails
    private fun isValidEmail(email: String): Boolean {
        val targetSuffix1 = "@myuct.ac.za"
        val targetSuffix2 = "@uct.ac.za"
        val emailLowerCase = email.lowercase()

        return emailLowerCase.endsWith(targetSuffix1) || emailLowerCase.endsWith(targetSuffix2)
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
    /** Method to check the entered information against the database to see if any of the fields already exist */
    private suspend fun isDuplicateEntry(email: String?, phone: String?, username: String?, userDao: AccountDao): Boolean {
        return withContext(Dispatchers.IO) {
            if (!email.isNullOrBlank()) {
                val existingEmailCount = userDao.getCountByEmail(email)
                if (existingEmailCount > 0) {
                    return@withContext true
                }
            }

            if (!phone.isNullOrBlank()) {
                val existingPhoneCount = userDao.getCountByPhone(phone)
                if (existingPhoneCount > 0) {
                    return@withContext true
                }
            }

            if (!username.isNullOrBlank()) {
                val existingUsernameCount = userDao.getCountByUsername(username)
                if (existingUsernameCount > 0) {
                    return@withContext true
                }
            }

            return@withContext false
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}

