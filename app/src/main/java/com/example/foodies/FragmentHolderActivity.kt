package com.example.foodies


import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphNavigator
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import classes.GuestViewModel
import classes.SharedViewModel
import classes.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FragmentHolderActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var guestViewModel: GuestViewModel
    private lateinit var storeViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_holder)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        guestViewModel = ViewModelProvider(this)[GuestViewModel::class.java]

        val isGuest = intent.getBooleanExtra("is_guest", false)
        guestViewModel.isGuest = isGuest
        storeViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        // calling the action bar
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        //Initialize the bottom navigation view
        //create bottom navigation view object
        val bottomNavigationView = findViewById<BottomNavigationView
                >(R.id.bottomNavigationView)
        val navController = findNavController(R.id.nav_fragment)


        if(!isGuest) { //if they are not a guest then continue as if they are a user
            val userId = intent.getIntExtra("user_id", -1)
            val userName = intent.getStringExtra("user_name")
            val userEmail = intent.getStringExtra("user_email")

            if (userEmail != null && userName != null) {

                CoroutineScope(Dispatchers.IO).launch {
                    val user = ApplicationCore.database.accountDao()
                        .getUserByEmailAndUsername(userEmail, userName)

                    withContext(Dispatchers.Main) {
                        if (user != null) {
                            val userViewModel =
                                ViewModelProvider(this@FragmentHolderActivity)[UserViewModel::class.java]
                            userViewModel.user = user
                        }
                    }
                }
            }
        }

        if (guestViewModel.isGuest) {
            val navGraph = navController.navInflater.inflate(R.navigation.nav_graph_guest)
            navController.graph = navGraph
            // Remove the "Rewards" menu item
            bottomNavigationView.menu.removeItem(R.id.rewardsFragment)
        }

        bottomNavigationView.setupWithNavController(navController)



    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }



}