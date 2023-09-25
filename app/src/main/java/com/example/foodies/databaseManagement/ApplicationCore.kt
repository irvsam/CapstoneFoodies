package com.example.foodies.databaseManagement

import android.app.Application
import android.util.Log
import androidx.room.Room
import classes.AppDatabase
import classes.DatabaseSeeder
import classes.Entities
import com.example.foodies.R
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
            Entities.MenuItem(
                id = 106,
                menuId = 1,
                name = "Peanut Chicken Curry (Full)",
                price = 80.00f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 107,
                menuId = 1,
                name = "Peanut chicken Curry (Half)",
                price = 42.0f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 108,
                menuId = 1,
                name = "Fish Stew (Full)",
                price = 80.00f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 113,
                menuId = 1,
                name = "Fish Stew(Half)",
                price = 45.00f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 109,
                menuId = 1,
                name = "Mutton Stew (Full)",
                price = 80.00f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 114,
                menuId = 1,
                name = "Mutton Stew (Half)",
                price = 45.00f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 110,
                menuId = 1,
                name = "Beef Stew (Full)",
                price = 80.00f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 115,
                menuId = 1,
                name = "Beef Stew(Half)",
                price = 42.00f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 111,
                menuId = 1,
                name = "Ulusu Stew (Full)",
                price = 80.00f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 116,
                menuId = 1,
                name = "Ulusu Stew(Half)",
                price = 42.00f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 112,
                menuId = 1,
                name = "Mince Stew (Full)",
                price = 80.00f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 117,
                menuId = 1,
                name = "Mince Stew(Half)",
                price = 42.00f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 118,
                menuId = 1,
                name = "Rice & Beans",
                price = 40.00f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 119,
                menuId = 1,
                name = "Jollof Rice",
                price = 40.00f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 120,
                menuId = 1,
                name = "Xhosa Chicken & Augusie",
                price = 40.00f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 121,
                menuId = 1,
                name = "+Chicken Curry",
                price = 26.00f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 122,
                menuId = 1,
                name = "+Peanut Chicken Curry",
                price = 26.00f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 123,
                menuId = 1,
                name = "+Fish Stew",
                price = 29.00f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 124,
                menuId = 1,
                name = "+Mutton Stew",
                price = 29.00f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 125,
                menuId = 1,
                name = "+Beef Stew",
                price = 26.00f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 126,
                menuId = 1,
                name = "+Ulusu Stew",
                price = 26.00f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 127,
                menuId = 1,
                name = "+Rice",
                price = 25.00f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 128,
                menuId = 1,
                name = "+Spiced Rice",
                price = 25.00f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 129,
                menuId = 1,
                name = "+Pap",
                price = 25.00f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 130,
                menuId = 1,
                name = "Samp & Beans",
                price = 25.00f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 131,
                menuId = 1,
                name = "Jappati (Roti)",
                price = 32.00f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 132,
                menuId = 1,
                name = "Sodas (330ml)",
                price = 12f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 133,
                menuId = 1,
                name = "Appletiser (330ml)",
                price = 15f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 134,
                menuId = 1,
                name = "Grapetiser (330ml)",
                price = 15f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 135,
                menuId = 1,
                name = "Valpre Mineral Water (330ml)",
                price = 12f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 136,
                menuId = 1,
                name = "Nestle Iced Tea (500ml)",
                price = 15f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 137,
                menuId = 1,
                name = "Herbal Tea (weight loss)",
                price = 15f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 138,
                menuId = 1,
                name = "Powerade (500ml)",
                price = 15f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 139,
                menuId = 1,
                name = "Bonaqua Flavoured Water (500ml)",
                price = 12f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 140,
                menuId = 1,
                name = "Bonaqua Pump (750ml)",
                price = 12f,
                inStock = true,
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
            Entities.MenuItem(
                id = 205,
                menuId = 2,
                name = "Cheese & Vienna Sandwich",
                price = 25f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 206,
                menuId = 2,
                name = "Steak & Egg Sandwich",
                price = 30f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 207,
                menuId = 2,
                name = "Steak & Cheese Sandwich",
                price = 35f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 208,
                menuId = 2,
                name = "Steak & Chips Sandwich",
                price = 45f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 209,
                menuId = 2,
                name = "Full House Sandwich",
                price = 50f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 210,
                menuId = 2,
                name = "Beef Burger",
                price = 30f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 211,
                menuId = 2,
                name = "Beef Burger & Chips",
                price = 40f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 212,
                menuId = 2,
                name = "Zinger Chicken Fillet Burger & Chips",
                price = 45f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 213,
                menuId = 2,
                name = "Masala Chicken Burger & Chips",
                price = 45f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 214,
                menuId = 2,
                name = "Crumbed Burger & Chips",
                price = 45f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 215,
                menuId = 2,
                name = "Chicken Wrap",
                price = 38f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 216,
                menuId = 2,
                name = "Steak Wrap",
                price = 40f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 217,
                menuId = 2,
                name = "Good Morning Wrap",
                price = 30f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 218,
                menuId = 2,
                name = "+ Chips to Wrap",
                price = 12f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 219,
                menuId = 2,
                name = "Chicken Strips & Chips",
                price = 48f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 220,
                menuId = 2,
                name = "Hot Chips (Medium)",
                price = 30f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 221,
                menuId = 2,
                name = "Hot Chips (Large)",
                price = 35f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 222,
                menuId = 2,
                name = "Worz Gatsby",
                price = 65f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 223,
                menuId = 2,
                name = "Vienna Gatsby",
                price = 65f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 224,
                menuId = 2,
                name = "Chicken Gatsby",
                price = 70f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 225,
                menuId = 2,
                name = "Steak Gatsby",
                price = 75f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 226,
                menuId = 2,
                name = "+ Cheese",
                price = 6f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 227,
                menuId = 2,
                name = "+Egg",
                price = 10f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 228,
                menuId = 2,
                name = "Worz Roll & Chips",
                price = 35f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 229,
                menuId = 2,
                name = "Worz Roll",
                price = 30f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 230,
                menuId = 2,
                name = "Chicken Roll & Chips",
                price = 45f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 231,
                menuId = 2,
                name = "Single Hot Dog",
                price = 35f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 232,
                menuId = 2,
                name = "Steak Roll & Chips",
                price = 45f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 233,
                menuId = 2,
                name = "Chips Roll",
                price = 25f,
                inStock = true,
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
                id = 301,
                menuId = 3,
                name = "Zinger Burger & Chips",
                price = 45f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 302,
                menuId = 3,
                name = "Chicken Burger & Chips",
                price = 45f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 303,
                menuId = 3,
                name = "Zinger Strips & Chips",
                price = 48f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 304,
                menuId = 3,
                name = "Chicken Burger",
                price = 35f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 305,
                menuId = 3,
                name = "Steak Burger",
                price = 38f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 306,
                menuId = 3,
                name = "Zinger Burger",
                price = 35f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 307,
                menuId = 3,
                name = "Vegetarian Burger",
                price = 35f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 308,
                menuId = 3,
                name = "Chicken Ciabatta",
                price = 40f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 309,
                menuId = 3,
                name = "Vegetarian Ciabatta",
                price = 40f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 310,
                menuId = 3,
                name = "Chicken Shwarma",
                price = 40f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 311,
                menuId = 3,
                name = "Steak Shwarma",
                price = 50f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 312,
                menuId = 3,
                name = "Chicken Sub",
                price = 40f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 313,
                menuId = 3,
                name = "Steak Sub",
                price = 50f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 314,
                menuId = 3,
                name = "Hot Dog (Single)",
                price = 30f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 315,
                menuId = 3,
                name = "Hot Dog (Double)",
                price = 35f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 316,
                menuId = 3,
                name = "Smoked Russian + Cheese & Chips",
                price = 35f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 317,
                menuId = 3,
                name = "Chicken Wrap",
                price = 38f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 318,
                menuId = 3,
                name = "Steak Wrap",
                price = 50f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 319,
                menuId = 3,
                name = "Zinger Wrap",
                price = 38f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 320,
                menuId = 3,
                name = "Assorted Pies",
                price = 22f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 321,
                menuId = 3,
                name = "Pie + Chips",
                price = 28f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 322,
                menuId = 3,
                name = "Hot Chips (Medium)",
                price = 28f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 323,
                menuId = 3,
                name = "Hot Chips (Medium)",
                price = 35f,
                inStock = true,
            ),
            Entities.MenuItem(
                id = 324,
                menuId = 3,
                name = "Chip Roll",
                price = 28f,
                inStock = true,
            ),

// Budget Rolls
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
                description = "Afriquezeen has a selection of curries and stews - both vegetarian and meat options. It is also Halaal.",
                menuId = 1,
                openTime = "09:00",
                closeTime = "17:00",
                dietaryReqId = 1,
                image = R.drawable.capemalay
            ),
            //Varisty Fast Foods store
            Entities.Vendor(
                id = 2,
                name = "Varsity Fast Foods",
                cuisine = "Meat",
                description = "Varsity Fast Foods has a selection of sandwiches, wraps and burgers. It is also Halaal.",
                menuId = 2,
                openTime = "09:00",
                closeTime = "17:00",
                dietaryReqId = 1,
                image = R.drawable.burgersss
            ),
            //Best Quality store
            Entities.Vendor(
                id = 3,
                name = "Best Quality",
                cuisine = "Meat",
                description = "Best Quality has a wide range of sandwiches, wraps, burgers, pies... They do have vegetarian options.",
                menuId = 3,
                openTime = "09:00",
                closeTime = "17:00",
                dietaryReqId = 1,
                image = R.drawable.friesss
            ),
            //Budget Rolls store
            Entities.Vendor(
                id = 4,
                name = "Budget Rolls",
                cuisine = "Meat",
                description = "Budget Rolls offers rolls with all types of fillings. They also have hot chocolate and coffee.",
                menuId = 4,
                openTime = "09:00",
                closeTime = "17:00",
                dietaryReqId = 1,
                image = R.drawable.rolls
            ),
            //Dans Health Shop store
            Entities.Vendor(
                id = 5,
                name = "Dan's Health Shop",
                cuisine = "Meat",
                description = "Dan's Health Shop offers drinks, snacks and sandwiches.",
                menuId = 5,
                openTime = "09:00",
                closeTime = "17:00",
                dietaryReqId = 1,
                image = R.drawable.juices
            ),
            //Campus Cafe store
            Entities.Vendor(
                id = 6,
                name = "Campus Cafe",
                cuisine = "Beverages",
                description = "Campus Cafe stocks all types of coffees and teas.",
                menuId = 6,
                openTime = "09:00",
                closeTime = "17:00",
                dietaryReqId = 1,
                image = R.drawable.coffees
            ),
            //Prashad store
            Entities.Vendor(
                id = 7,
                name = "Prashad",
                cuisine = "Meat",
                description = "Prashad stocks samoosas, curries, snacks and desserts.",
                menuId = 7,
                openTime = "09:00",
                closeTime = "17:00",
                dietaryReqId = 1,
                image = R.drawable.samoosa
            ),

            // Add more entities as needed
        )
       seedVendorTable(vendorEntitiesToSeed)

        //seedTestUser(testUser)
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



