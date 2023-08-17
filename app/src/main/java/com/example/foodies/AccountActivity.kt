package com.example.foodies

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AccountActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)



        val vendorButton = findViewById<Button>(R.id.v_button)

        // Set an OnClickListener to navigate to vendor list
        /*vendorButton.setOnClickListener {
            val intent = Intent(this, VendorListActivity::class.java)
            startActivity(intent)
        }*/
        // Set click listener for the logout button

        val signOutButton = findViewById<Button>(R.id.sign_out)
        signOutButton.setOnClickListener {
            // Navigate to MainActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            // Optional: finish the LoginActivity if you don't want to come back to it using the back button
        }
    }
}