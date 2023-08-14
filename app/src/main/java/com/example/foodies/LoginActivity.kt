package com.example.foodies

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val registerLinkTextView = findViewById<TextView>(R.id.registerLink)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Handle the click event, navigate to the registration screen

            }
        }

        // Set the ClickableSpan to the TextView
        val spannableString = SpannableString(registerLinkTextView.text)
        spannableString.setSpan(clickableSpan, 23, 40, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        registerLinkTextView.text = spannableString
        registerLinkTextView.movementMethod = LinkMovementMethod.getInstance()

        // Set click listener for the login button
        val loginButton = findViewById<Button>(R.id.login_btn)
        loginButton.setOnClickListener {
            // Navigate to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
             // Optional: finish the LoginActivity if you don't want to come back to it using the back button
        }
    }
}