package com.example.foodies


import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import classes.GuestViewModel
import classes.VendorManagementViewModel
import classes.SharedViewModel
import classes.AccountViewModel
import classes.StoreViewModel
import com.example.foodies.databaseManagement.ApplicationCore
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FragmentHolderActivity : AppCompatActivity() {
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var vendorViewModel: StoreViewModel
    private lateinit var guestViewModel: GuestViewModel
    private lateinit var storeViewModel: SharedViewModel
    private lateinit var vendorManagementViewModel: VendorManagementViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fragment_holder)

        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        guestViewModel = ViewModelProvider(this)[GuestViewModel::class.java]
        vendorViewModel = ViewModelProvider(this)[StoreViewModel::class.java]
        storeViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        vendorManagementViewModel = ViewModelProvider(this)[VendorManagementViewModel::class.java]

        val isGuest = intent.getBooleanExtra("is_guest", false)
        val isVendor = intent.getBooleanExtra("is_vendor",false)

        guestViewModel.isGuest = isGuest
        vendorManagementViewModel.isVendor = isVendor

        // calling the action bar
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        //Initialize the bottom navigation view
        //create bottom navigation view object

        Log.d(ContentValues.TAG, "setting up the nav bar")
        val bottomNavigationView = findViewById<BottomNavigationView
                >(R.id.bottomNavigationView)
        val navController = findNavController(R.id.nav_fragment)
        bottomNavigationView.setupWithNavController(navController)

        if(!isGuest && !isVendor) { //if they logged in then set the user view model
            bottomNavigationView.menu.removeItem(R.id.managementFragment)
            setUser()
            Log.d(ContentValues.TAG, "not guest or vendor")
        }
        else if (vendorManagementViewModel.isVendor){
            setVendor()
            Log.d("Vendor","Entered vendor statement")
            val navGraph = navController.navInflater.inflate((R.navigation.nav_graph_vendor))
            navController.graph = navGraph
            bottomNavigationView.menu.removeItem(R.id.rewardsFragment)
            bottomNavigationView.menu.removeItem(R.id.browseFragment)
        }

        else if (guestViewModel.isGuest) {
            Log.d(ContentValues.TAG, "guest")
            val navGraph = navController.navInflater.inflate(R.navigation.nav_graph_guest)
            navController.graph = navGraph
            bottomNavigationView.menu.removeItem(R.id.rewardsFragment)
            bottomNavigationView.menu.removeItem(R.id.managementFragment)


        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setUser(){
        val userName = intent.getStringExtra("user_name")
        val userEmail = intent.getStringExtra("user_email")

        if (userEmail != null && userName != null) {

            CoroutineScope(Dispatchers.IO).launch {
                val user = ApplicationCore.database.accountDao()
                    .getUserByEmailAndUsername(userEmail, userName)

                withContext(Dispatchers.Main) {
                    if (user != null) {
                        val accountViewModel = ViewModelProvider(this@FragmentHolderActivity)[AccountViewModel::class.java]
                        accountViewModel.user = user
                    }
                }
            }
        }

    }

    private fun setVendor(){
        val userName = intent.getStringExtra("user_name")
        val userEmail = intent.getStringExtra("user_email")

        if (userEmail != null && userName != null) {

            CoroutineScope(Dispatchers.IO).launch {
                val user = ApplicationCore.database.accountDao()
                    .getUserByEmailAndUsername(userEmail, userName)

                withContext(Dispatchers.Main) {
                    if (user != null) {
                        //vendorViewModel = ViewModelProvider(this@FragmentHolderActivity)[StoreViewModel::class.java]
                        //logged on vendor user
                        //vendorViewModel.user = user
                        //set the vendor store
                        //vendorViewModel.vendor = ApplicationCore.database.accountDao().getVendorStore(user.vendorId)
                        //set the logged on vendors store

                        vendorManagementViewModel.user = user
                        vendorManagementViewModel.setVendor()
                        // set the menu items
                        vendorManagementViewModel.setMenuItems(ApplicationCore.database.menuItemDao().getMenuItemsByMenuId(vendorManagementViewModel.vendor!!.menuId))}

                }
            }
        }
    }



}