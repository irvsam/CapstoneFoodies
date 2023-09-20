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

/** this activity kind of acts as the main activity which holds all the fragments that get opened
 * it is the first to open from the login page and then it calls each fragment when they are navigated to
 * */
class FragmentHolderActivity : AppCompatActivity() {
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var vendorViewModel: StoreViewModel
    private lateinit var guestViewModel: GuestViewModel
    private lateinit var storeViewModel: SharedViewModel
    private lateinit var vendorManagementViewModel: VendorManagementViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fragment_holder)
        /** set up all the shared view models we need */
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        guestViewModel = ViewModelProvider(this)[GuestViewModel::class.java]
        vendorViewModel = ViewModelProvider(this)[StoreViewModel::class.java]
        storeViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        vendorManagementViewModel = ViewModelProvider(this)[VendorManagementViewModel::class.java]

        /** these get passed through on login */
        val isGuest = intent.getBooleanExtra("is_guest", false)
        val isVendor = intent.getBooleanExtra("is_vendor",false)
        guestViewModel.isGuest = isGuest
        vendorManagementViewModel.isVendor = isVendor


        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        /** set up the bottom navigation*/
        val bottomNavigationView = findViewById<BottomNavigationView
                >(R.id.bottomNavigationView)
        val navController = findNavController(R.id.nav_fragment)
        bottomNavigationView.setupWithNavController(navController)

        /** this is a general user account logged in*/
        if(!isGuest && !isVendor) {
            bottomNavigationView.menu.removeItem(R.id.managementFragment)
            setUser()
            Log.d(ContentValues.TAG, "not guest or vendor")
        }

        /** this is a vendor user */
        else if (vendorManagementViewModel.isVendor){
            setVendor()
            Log.d("Vendor","Entered vendor statement")
            val navGraph = navController.navInflater.inflate((R.navigation.nav_graph_vendor))
            navController.graph = navGraph
            bottomNavigationView.menu.removeItem(R.id.rewardsFragment)
            bottomNavigationView.menu.removeItem(R.id.browseFragment)
        }
        /** this is a guest user */
        else if (guestViewModel.isGuest) {
            Log.d(ContentValues.TAG, "guest")
            val navGraph = navController.navInflater.inflate(R.navigation.nav_graph_guest)
            navController.graph = navGraph
            bottomNavigationView.menu.removeItem(R.id.rewardsFragment)
            bottomNavigationView.menu.removeItem(R.id.managementFragment)

        }
    }

    /** set up the back button behavior*/
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    /** set up the user in the shared view model from the data in the intent */
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

    /** set up the vendor account in the shared view model from the data in the intent*/
    private fun setVendor(){
        val userName = intent.getStringExtra("user_name")
        val userEmail = intent.getStringExtra("user_email")

        if (userEmail != null && userName != null) {
            CoroutineScope(Dispatchers.IO).launch {
                val user = ApplicationCore.database.accountDao()
                    .getUserByEmailAndUsername(userEmail, userName)
                withContext(Dispatchers.Main) {
                    if (user != null) {
                        vendorManagementViewModel.user = user
                        vendorManagementViewModel.setVendor()
                        vendorManagementViewModel.setMenuItems(ApplicationCore.database.menuItemDao().getMenuItemsByMenuId(vendorManagementViewModel.vendor!!.menuId))}

                }
            }
        }
    }



}