package com.example.foodies

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class VendorListActivity : AppCompatActivity() {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_vendor_list)

        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        val backToHomeButton = findViewById<Button>(R.id.backToHome_button)
        backToHomeButton.setOnClickListener {
            sharedViewModel.onBackToHomeClick()
        }
    }

}