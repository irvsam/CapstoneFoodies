package com.example.foodies


import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import classes.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class FragmentHolderActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    // This method is called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_holder)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        // calling the action bar
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)


        val user = userViewModel.user
        Log.d("FragmentHolderActivity", "User ID: ${user?.id}")
        Log.d("FragmentHolderActivity", "User Name: ${user?.username}")
        Log.d("FragmentHolderActivity", "User Email: ${user?.email}")
        //Initialize the bottom navigation view
        //create bottom navigation view object
        val bottomNavigationView = findViewById<BottomNavigationView
                >(R.id.bottomNavigationView)
        val navController = findNavController(R.id.nav_fragment)
        bottomNavigationView.setupWithNavController(navController)


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


}