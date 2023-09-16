package com.example.foodies


import android.content.ContentValues
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
import classes.MenuItemViewModel
import classes.SharedViewModel
import classes.UserViewModel
import classes.VendorViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FragmentHolderActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var vendorViewModel: VendorViewModel
    private lateinit var guestViewModel: GuestViewModel
    private lateinit var storeViewModel: SharedViewModel
    private lateinit var menuItemViewModel: MenuItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        populateStores()
        setContentView(R.layout.activity_fragment_holder)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        guestViewModel = ViewModelProvider(this)[GuestViewModel::class.java]
        vendorViewModel = ViewModelProvider(this)[VendorViewModel::class.java]
        storeViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        menuItemViewModel = ViewModelProvider(this)[MenuItemViewModel::class.java]

        val isGuest = intent.getBooleanExtra("is_guest", false)
        val isVendor = intent.getBooleanExtra("is_vendor",false)

        guestViewModel.isGuest = isGuest
        vendorViewModel.isVendor = isVendor

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
        else if (vendorViewModel.isVendor){
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
                        val userViewModel = ViewModelProvider(this@FragmentHolderActivity)[UserViewModel::class.java]
                        userViewModel.user = user
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
                        vendorViewModel = ViewModelProvider(this@FragmentHolderActivity)[VendorViewModel::class.java]
                        vendorViewModel.user = user
                        Log.d("User",user.username)
                        vendorViewModel.vendor = ApplicationCore.database.accountDao().getVendorStore(user.vendorId)
                        menuItemViewModel.menuItems = ApplicationCore.database.menuItemDao().getMenuItemsByMenuId(vendorViewModel.vendor!!.menuId)
                        Log.d("Vendor Store",vendorViewModel.vendor!!.name)
                    }
                }
            }
        }
    }

    private fun populateStores() {

        CoroutineScope(Dispatchers.IO).launch {
            Log.d(ContentValues.TAG,"populating the stores" )
            val allStores = ApplicationCore.database.vendorDao().getAllVendors()
            withContext(Dispatchers.Main){
                if (storeViewModel.storeList.isEmpty()) {
                    if(allStores.isNotEmpty()){
                        for (store in allStores){
                            storeViewModel.storeList.add(store)
                        }
                    }
                }
            }

        }
    }

}