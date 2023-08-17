package com.example.foodies

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavHomeActivity : AppCompatActivity() {

    private val accountActivity = AccountActivity()
    private val homeActivity = MainActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_home)
        //Initialize the bottom navigation view
        //create bottom navigation view object
        val bottomNavigationView = findViewById<BottomNavigationView
                >(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment_container)
        bottomNavigationView.setupWithNavController(navController)
        // we need the activities to be fragments before we can navigate between

    }
}