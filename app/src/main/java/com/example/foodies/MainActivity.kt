package com.example.foodies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import classes.DietaryReq
import classes.Menu
import classes.MenuItem
import classes.Review
import classes.STORE_EXTRA
import classes.Store
import classes.StoreClickListener
import classes.storeList
import classes.storeRecyclerViewAdapter
import com.example.foodies.databinding.ActivityMainBinding
import com.example.foodies.databinding.StoreCardCellBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.sql.Time

class MainActivity : AppCompatActivity(), StoreClickListener {

    private lateinit var binding: ActivityMainBinding

    private var storesPopulated = false  // Flag to track whether stores have been populated


    private var imageList : List<Int> = listOf(R.drawable.cc,R.drawable.afriquezeen)
    //private val filterCoffee:MenuItem = MenuItem("Filter Coffee",14.00,"warm",true)
    //private val icedCoffee: MenuItem = MenuItem("Iced Coffee",15.00,"cold",true)
    private var campusCafeMenu : Menu = Menu()
    private var afriquezeenMenu: Menu = Menu()
    private var ccReviewList : ArrayList<Review> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //only populate if it is the first time opening this activity
        if (!storesPopulated) {
            populateStores()
            storesPopulated = true  // Set the flag to true after populating stores for the first time
        }
        val mainActivity = this
        binding.storeRecyclerView.apply {
            layoutManager = GridLayoutManager(applicationContext,1)
            adapter = storeRecyclerViewAdapter(storeList,mainActivity)
        }

        // Find the account button by its ID
        val accountButton = findViewById<Button>(R.id.account_button)

        // Set an OnClickListener to navigate to AccountActivity
        accountButton.setOnClickListener {
            val intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
        }
    }

    private fun populateStores(){
        //campusCafeMenu.addItem(filterCoffee)
        //campusCafeMenu.addItem(icedCoffee)
        val campusCafe:Store = Store("Campus Cafe","Beverages", campusCafeMenu, 4.2, Time(8,15,0),Time(16,15,0),DietaryReq.VEGETARIAN,ccReviewList,imageList[0])
        val afriquezeen:Store = Store("Afriquezeen","Hearty meals", afriquezeenMenu, 4.8, Time(8,15,0),Time(16,15,0),DietaryReq.NUT_FREE,ccReviewList,imageList[1])
        storeList.add(campusCafe)
        storeList.add(afriquezeen)
    }

    override fun onClick(store: Store) {
        val intent = Intent(applicationContext,StoreDetailsActivity::class.java)
        intent.putExtra(STORE_EXTRA,store.name)
        startActivity(intent)

    }
}
