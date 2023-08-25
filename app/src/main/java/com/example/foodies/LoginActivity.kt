package com.example.foodies

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import classes.Entities
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

        val user = Entities.User(email = "user@example.com", password = "password", type = "user", rewardPoints = 100)

        // launch a coroutine to add user in the background
        lifecycleScope.launch {
            insertUserInBackground(user)
        }

        val registerLinkTextView = findViewById<TextView>(R.id.registerLink)

        // Set the ClickableSpan to the TextView
        val spannableString = SpannableString(registerLinkTextView.text)
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                //TODO Handle the click event, open the registration activity
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }, 23, 45, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        registerLinkTextView.text = spannableString
        registerLinkTextView.movementMethod = LinkMovementMethod.getInstance()



        // Set click listener for the login button
        //TODO this should not just let them in but for prototype it might be good enough
        val loginButton = findViewById<Button>(R.id.login_btn)
        loginButton.setOnClickListener {

            emailEditedText = findViewById(R.id.email_input)
            passwordEditedText = findViewById(R.id.password_input)

            // Navigate to MainActivity
            val email = emailEditedText.text.toString()
            val password = passwordEditedText.text.toString()

            if (email=="wkrrya001" && password=="ryan") {  //If login credentials are correct open main screen
                //set logged in boolean
                //sharedViewModel.loggedIn = true
                val intent = Intent(this, FragmentHolderActivity::class.java)
                startActivity(intent)
                //println("Logged in")
                // Optional: finish the LoginActivity if you don't want to come back to it using the back button
            }
            else{
                //Reset text boxes to ask for input again
                emailEditedText.text = null
                passwordEditedText.text = null
                emailEditedText.requestFocus()  //Bring cursor back to email box
                Toast.makeText(this,"Invalid login, please try again",Toast.LENGTH_SHORT).show()
            }
        }
        // Set click listener for the continue as guest button
        //TODO we must make this only allow guest functionality
        val guestButton = findViewById<Button>(R.id.guestCont_btn)
        guestButton.setOnClickListener {
            //set guest boolean
            //sharedViewModel.guest=true
            // Navigate to MainActivity
            val intent = Intent(this, FragmentHolderActivity::class.java)
            startActivity(intent)
            // Optional: finish the LoginActivity if you don't want to come back to it using the back button
        }
    }
}
// do this on a separate thread because if it's on the main thread
// it could cause delayed responses
private suspend fun insertUserInBackground(user: Entities.User) {
    withContext(Dispatchers.IO) {
        // Perform the database operation on the IO dispatcher
        ApplicationCore.database.accountDao().insertUser(user)
    }
}