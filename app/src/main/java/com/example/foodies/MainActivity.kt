package com.example.foodies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import classes.DietaryReq
import classes.Menu
import classes.Review
import classes.Store
import java.sql.Time

class MainActivity : AppCompatActivity() {

    var storeList : ArrayList<Store> = ArrayList()
    var imageList : List<Int> = listOf(R.drawable.campus_coffee,R.drawable.afriquezeen)
    var campusCafeMenu : Menu = Menu()
    var ccReviewList : ArrayList<Review> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        populateStores()

        /*if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_container, VendorListFragment())
            transaction.commit()
        }*/
    }

    private fun populateStores(){
        val campusCafe:Store = Store("Campus Cafe","Beverages", campusCafeMenu, 4.2, Time(8,15,0),Time(16,15,0),DietaryReq.VEGETARIAN,ccReviewList,imageList[0])
        val afriquezeen:Store = Store("Afriquezeen","Hearty meals", campusCafeMenu, 4.8, Time(8,15,0),Time(16,15,0),DietaryReq.NUT_FREE,ccReviewList,imageList[1])

    }
}
