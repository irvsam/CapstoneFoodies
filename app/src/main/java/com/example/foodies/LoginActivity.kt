package com.example.foodies

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import classes.Entities
import classes.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditedText: EditText
    private lateinit var passwordEditedText: EditText

    //private val sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val registerLinkTextView = findViewById<TextView>(R.id.registerLink)

        // Set the ClickableSpan to the TextView
        val spannableString = SpannableString(registerLinkTextView.text)
        spannableString.setSpan(object : ClickableSpan() {

            override fun onClick(widget: View) {

                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }, 23, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        registerLinkTextView.text = spannableString
        registerLinkTextView.movementMethod = LinkMovementMethod.getInstance()

        // Set click listener for the login button
        val loginButton = findViewById<Button>(R.id.login_btn)
        loginButton.setOnClickListener {

            emailEditedText = findViewById(R.id.email_input)
            passwordEditedText = findViewById(R.id.password_input)

            // Navigate to MainActivity
            val email = emailEditedText.text.toString()
            val password = passwordEditedText.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                val user = ApplicationCore.database.accountDao().getUserByEmailAndPassword(email, password)
                withContext(Dispatchers.Main) {
                    if (user != null) {  //If login credentials are correct open main screen
                        showToast("Login Successful")

                        //pass the user through in the intent
                        val intent = Intent(this@LoginActivity, FragmentHolderActivity::class.java)
                        intent.putExtra("user", user.id)
                        intent.putExtra("user_name", user.username)
                        intent.putExtra("user_email", user.email)

                        startActivity(intent)

                    } else {
                        showToast("Invalid credentials. Please try again.")
                    }
                }
            }

        }
        val guestButton = findViewById<Button>(R.id.guestCont_btn)
        guestButton.setOnClickListener {
            val intent = Intent(this, FragmentHolderActivity::class.java)
            intent.putExtra("is_guest", true)
            startActivity(intent)
        }
    }
        private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
