package com.example.foodies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import classes.DietaryReq
import classes.Menu
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


    private var imageList : List<Int> = listOf(R.drawable.cc,R.drawable.afriquezeen)
    private var campusCafeMenu : Menu = Menu()
    private var ccReviewList : ArrayList<Review> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        populateStores()
        val mainActivity = this
        binding.storeRecyclerView.apply {
            layoutManager = GridLayoutManager(applicationContext,1)
            adapter = storeRecyclerViewAdapter(storeList,mainActivity)
        }
        /*if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_container, VendorListFragment())
            transaction.commit()
        }*/
    }

    private fun populateStores(){
        val campusCafe:Store = Store("Campus Cafe","Beverages", campusCafeMenu, 4.2, Time(8,15,0),Time(16,15,0),DietaryReq.VEGETARIAN,ccReviewList,imageList[0])
        val afriquezeen:Store = Store("Afriquezeen","Hearty meals", campusCafeMenu, 4.8, Time(8,15,0),Time(16,15,0),DietaryReq.NUT_FREE,ccReviewList,imageList[1])
        storeList.add(campusCafe)
        storeList.add(afriquezeen)
    }

    override fun onClick(store: Store) {
        val intent = Intent(applicationContext,StoreDetailsActivity::class.java)
        intent.putExtra(STORE_EXTRA,store.name)
        startActivity(intent)

    }
}
