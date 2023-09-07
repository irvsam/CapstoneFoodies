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
            Entities.MenuItem(
                id = 11,
                menuId = 1,
                name = "Jollof",
                price = 40.00f
            ),
            Entities.MenuItem(
                id = 12,
                menuId = 2,
                name = "Beef Burger",
                price = 40.00f
            ),
            Entities.MenuItem(
                id = 13,
                menuId = 3,
                name = "Chicken sub",
                price = 40.00f
            ),
            Entities.MenuItem(
                id = 14,
                menuId = 4,
                name = "Chicken Mayo Rolls",
                price = 40.00f
            ),
            Entities.MenuItem(
                id = 15,
                menuId = 5,
                name = "Dried Fruit",
                price = 10.00f
            ),
            Entities.MenuItem(
                id = 16,
                menuId = 6,
                name = "Filter Coffee",
                price = 14.00f
            ),
            Entities.MenuItem(
                id = 17,
                menuId = 7,
                name = "Wrap",
                price = 45.00f
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
                rating = 4.0,
                openTime = "7:00",
                closeTime = "18:00",
                dietaryReqId = 1
            ),
            //Varisty Fast Foods store
            Entities.Vendor(
                id = 2,
                name = "Varsity Fast Foods",
                cuisine = "Meat",
                menuId = 2,
                rating = 5.0,
                openTime = "9:00",
                closeTime = "17:00",
                dietaryReqId = 1
            ),
            //Best Quality store
            Entities.Vendor(
                id = 3,
                name = "Best Quality",
                cuisine = "Meat",
                menuId = 3,
                rating = 5.0,
                openTime = "9:00",
                closeTime = "17:00",
                dietaryReqId = 1
            ),
            //Budget Rolls store
            Entities.Vendor(
                id = 4,
                name = "Budget Rolls",
                cuisine = "Meat",
                menuId = 4,
                rating = 5.0,
                openTime = "9:00",
                closeTime = "17:00",
                dietaryReqId = 1
            ),
            //Dans Health Shop store
            Entities.Vendor(
                id = 5,
                name = "Dan's Health Shop",
                cuisine = "Meat",
                menuId = 5,
                rating = 5.0,
                openTime = "9:00",
                closeTime = "17:00",
                dietaryReqId = 1
            ),
            //Campus Cafe store
            Entities.Vendor(
                id = 6,
                name = "Campus Coffee",
                cuisine = "Beverages",
                menuId = 6,
                rating = 5.0,
                openTime = "9:00",
                closeTime = "17:00",
                dietaryReqId = 1
            ),
            //Prashad store
            Entities.Vendor(
                id = 7,
                name = "Prashad",
                cuisine = "Meat",
                menuId = 7,
                rating = 5.0,
                openTime = "9:00",
                closeTime = "17:00",
                dietaryReqId = 1
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



