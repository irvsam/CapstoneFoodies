package com.example.foodies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewVendorsButton = findViewById<Button>(R.id.button_ViewVendors)
        viewVendorsButton.setOnClickListener {
            val intent = Intent(this, VendorListActivity::class.java)
            startActivity(intent)
        }
    }
}