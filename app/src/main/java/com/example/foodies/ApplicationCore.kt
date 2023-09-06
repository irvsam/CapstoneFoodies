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
            Entities.Menu(
                id = 1,
                name = "AfriQuezeen"
            )
        )

        // This is where the dietaryRequirements are created for prepopulation
        val dietaryReqEntitiesToSeed = listOf(
            Entities.DietaryRequirement(
                id = 1,
                type = "None"
            )
        )
        seedMenuTable(menuEntitiesToSeed)
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
            )

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



