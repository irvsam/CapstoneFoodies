package com.example.foodies

import android.app.Application
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import classes.AppDatabase
import classes.DatabaseSeeder
import classes.Entities
import com.example.foodies.ApplicationCore.Companion.database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ApplicationCore : Application() {

    companion object {
        lateinit var database: AppDatabase
        lateinit var dbHelper: DatabaseHelper
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("ApplicationCore", "onCreate() method is being executed")

        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app-database")
            .fallbackToDestructiveMigration()
            .build()
        // TODO: finish writing out the vendor details
        // Here is where we create the list of MenuEntities that need to prepopulate the database
        val menuEntitiesToSeed = listOf(

            //Various store menu's
            Entities.Menu(
                id = 1,
                name = "AfriQuezeen"
            ),
            Entities.Menu(
                id = 2,
                name = "Varsity Fast Foods"
            ),
            Entities.Menu(
                id = 3,
                name = "Best Quality"
            ),
            Entities.Menu(
                id = 4,
                name = "Budget Rolls"
            ),
            Entities.Menu(
                id = 5,
                name = "Dan's Health Shop"
            ),
            Entities.Menu(
                id = 6,
                name = "Campus Cafe"
            ),
            Entities.Menu(
                id = 7,
                name = "Prashad"
            ),
        )

        //List of menuItems that need to be seeded
        val menuItemEntitiesToSeed = listOf(

            // Afriquezeen menu items
            Entities.MenuItem(
                id = 100,
                menuId = 1,
                name = "Jollof",
                price = 40.00f,
                inStock = true
            ),
            Entities.MenuItem(
                id = 101,
                menuId = 1,
                name= "Afri Veg Platter (2 Starch & 2 Veg) ",
                price = 70.00f,
                inStock = true
            ),
            Entities.MenuItem(
                id = 102,
                menuId = 1,
                name= "Afri Veg Platter (2 Starch & 1 Veg)",
                price = 55.00f,
                inStock = true
            ),
            Entities.MenuItem(
                id = 103,
                menuId = 1,
                name= "Half Portion (1 Starch & 2 Veg ",
                price = 43.00f,
                inStock = true
            ),
            Entities.MenuItem(
                id = 104,
                menuId = 1,
                name= "Chicken Curry (Full) ",
                price = 80.00f,
                inStock = true
            ),
            Entities.MenuItem(
                id = 105,
                menuId = 1,
                name= "Chicken Curry (Half)",
                price = 42.00f,
                inStock = true
            ),

            //Varsity Fast Foods menu items
            Entities.MenuItem(
                id = 200,
                menuId = 2,
                name = "Beef Burger",
                price = 40.00f,
                inStock = true
            ),
            Entities.MenuItem(
                id = 201,
                menuId = 2,
                name= "Cheese Sandwich ",
                price = 15.00f,
                inStock = true
            ),
            Entities.MenuItem(
                id = 202,
                menuId = 2,
                name= "Cheese & Tomato Sandwich",
                price = 20.00f,
                inStock = true
            ),
            Entities.MenuItem(
                id = 203,
                menuId =2 ,
                name= "Chicken Mayo Sandwich",
                price = 25.00f,
                inStock = true
            ),
            Entities.MenuItem(
                id = 204,
                menuId = 2,
                name= "Steak Sandwich ",
                price = 25.00f,
                inStock = true
            ),

            //Best Quality menu Items
            Entities.MenuItem(
                id = 300,
                menuId = 3,
                name = "Chicken sub",
                price = 40.00f,
                inStock = true
            ),


            Entities.MenuItem(
                id = 400,
                menuId = 4,
                name = "Chicken Mayo Rolls",
                price = 40.00f,
                inStock = true
            ),


            Entities.MenuItem(
                id = 500,
                menuId = 5,
                name = "Dried Fruit",
                price = 10.00f,
                inStock = true
            ),


            Entities.MenuItem(
                id = 600,
                menuId = 6,
                name = "Filter Coffee",
                price = 14.00f,
                inStock = true
            ),


            Entities.MenuItem(
                id = 700,
                menuId = 7,
                name = "Wrap",
                price = 45.00f,
                inStock = true
            ),
        )

        // This is where the dietaryRequirements are created for prepopulation
        val dietaryReqEntitiesToSeed = listOf(
            Entities.DietaryRequirement(
                id = 1,
                type = "None"
            )
        )
        seedMenuTable(menuEntitiesToSeed)
        seedMenuItemTable(menuItemEntitiesToSeed)
        seedDietaryReqTable(dietaryReqEntitiesToSeed)

        // Seed Vendor Entities
        val vendorEntitiesToSeed = listOf(
            Entities.Vendor(
                id = 1,
                name = "AfriQuezeen",
                cuisine = "Meat",
                menuId = 1,
                openTime = "7:00",
                closeTime = "18:00",
                dietaryReqId = 1,
                image = R.drawable.curry
            ),
            //Varisty Fast Foods store
            Entities.Vendor(
                id = 2,
                name = "Varsity Fast Foods",
                cuisine = "Meat",
                menuId = 2,
                openTime = "9:00",
                closeTime = "17:00",
                dietaryReqId = 1,
                image = R.drawable.burger
            ),
            //Best Quality store
            Entities.Vendor(
                id = 3,
                name = "Best Quality",
                cuisine = "Meat",
                menuId = 3,
                openTime = "9:00",
                closeTime = "17:00",
                dietaryReqId = 1,
                image = R.drawable.chips
            ),
            //Budget Rolls store
            Entities.Vendor(
                id = 4,
                name = "Budget Rolls",
                cuisine = "Meat",
                menuId = 4,
                openTime = "9:00",
                closeTime = "17:00",
                dietaryReqId = 1,
                image = R.drawable.roll
            ),
            //Dans Health Shop store
            Entities.Vendor(
                id = 5,
                name = "Dan's Health Shop",
                cuisine = "Meat",
                menuId = 5,
                openTime = "9:00",
                closeTime = "17:00",
                dietaryReqId = 1,
                image = R.drawable.juices
            ),
            //Campus Cafe store
            Entities.Vendor(
                id = 6,
                name = "Campus Coffee",
                cuisine = "Beverages",
                menuId = 6,
                openTime = "9:00",
                closeTime = "17:00",
                dietaryReqId = 1,
                image = R.drawable.coffees
            ),
            //Prashad store
            Entities.Vendor(
                id = 7,
                name = "Prashad",
                cuisine = "Meat",
                menuId = 7,
                openTime = "9:00",
                closeTime = "17:00",
                dietaryReqId = 1,
                image = R.drawable.samoosa
            ),

            // Add more entities as needed
        )
       seedVendorTable(vendorEntitiesToSeed)
    }

    // Inserts menu items into the DB on a background thread
    private fun seedMenuTable(entitiesToSeed: List<Entities.Menu>) {
        GlobalScope.launch(Dispatchers.Main) {
            DatabaseSeeder.seedMenuTable(database, entitiesToSeed)
        }
    }

    //Inserts the various menuItems items into the DB on a background thread
    private fun seedMenuItemTable(entitiesToSeed: List<Entities.MenuItem>) {
        GlobalScope.launch(Dispatchers.Main) {
            DatabaseSeeder.seedMenuItemTable(database,entitiesToSeed)
        }
    }

    // Inserts dietReq items into the DB on a background thread
    private fun seedDietaryReqTable(entitiesToSeed: List<Entities.DietaryRequirement>) {
        GlobalScope.launch(Dispatchers.Main) {
            DatabaseSeeder.seedDietaryReqTable(database, entitiesToSeed)
        }
    }
    // Inserts vendor items into the DB on a background thread
    private fun seedVendorTable(entitiesToSeed: List<Entities.Vendor>) {
        GlobalScope.launch(Dispatchers.Main) {
            DatabaseSeeder.seedVendorTable(database, entitiesToSeed)
        }
    }

}



