package com.example.foodies

import HomeFragment
import VendorListFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        sharedViewModel.onViewVendorsAction.observe(this) {

            // must create a new transaction object every time you need one
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_container, VendorListFragment())
            transaction.addToBackStack(null)
            transaction.commit()

        }

        if (savedInstanceState == null) {

                val transaction = supportFragmentManager.beginTransaction()
                // A 'transaction' allows you to add, replace, and remove fragments
                // here we are replacing the fragment
                transaction.replace(R.id.main_container, homeFragment)
                    // this enables the user to use the back button to return to the previous page
                    .addToBackStack(null)
                    // commit() applies the changes
                    .commit()
        }

    }
}