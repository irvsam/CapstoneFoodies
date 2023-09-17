package com.example.foodies.databaseManagement

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "food_review.db"
        private const val DATABASE_VERSION = 2
    }

    override fun onCreate(db: SQLiteDatabase) {
        createTables(db)
        seedDatabase(db)
    }

    private fun createTables(db: SQLiteDatabase) {
        // Create your tables here
        db.execSQL(CREATE_VENDOR_TABLE)
        db.execSQL(CREATE_MENU_TABLE)
        db.execSQL(CREATE_DIETARY_REQ_TABLE)
        db.execSQL(CREATE_MENU_ITEM_TABLE)
        db.execSQL(CREATE_REVIEW_TABLE)
        db.execSQL(CREATE_USER_TABLE)
        db.execSQL(CREATE_SCANS_TABLE)
    }

     fun seedDatabase(db : SQLiteDatabase) {
        val campusCafe = ContentValues().apply {
            put("id", 1)
            put("name", "Campus Cafe")
            put("cuisine", "Hot Drinks")
            put("menu_id", 6)
            put("rating", 3.3)
            put("openTime", "07:00")
            put("closeTime", "18:00")
            put("dietaryReq_id", 1)
        }
        db.insert("vendor", null, campusCafe)



        // Creating a User for testing:
        // for ease of testing:
        // email = 1
        // password = p
        val testUserValues = ContentValues().apply {
            put("id", 1)
            put("email", "1")
            put("password", "p")
            put("rewardPoints", 200)
        }
        db.insert("user",null, testUserValues)
    }

    private val CREATE_USER_TABLE = """
        CREATE TABLE user (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        email TEXT,
        password TEXT,
        rewardPoints INTEGER
        )
    """.trimIndent()

    private val CREATE_VENDOR_TABLE = """
        CREATE TABLE vendor (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT,
            cuisine TEXT,
            menu_id INTEGER,
            rating REAL,
            openTime TEXT,
            closeTime TEXT,
            dietaryReq_id INTEGER,
            FOREIGN KEY(menu_id) REFERENCES menu(id),
            FOREIGN KEY(dietaryReq_id) REFERENCES dietary_req(id)
        )
    """.trimIndent()

    private val CREATE_DIETARY_REQ_TABLE = """
            CREATE TABLE dietary_req (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            type TEXT
        )
    """.trimIndent()

    private val CREATE_MENU_TABLE = """
        CREATE TABLE menu (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT
    )
""".trimIndent()

    private val CREATE_MENU_ITEM_TABLE = """
        CREATE TABLE menuitem (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            menu_id INTEGER,
            name TEXT,
            price REAL,
            FOREIGN KEY(menu_id) REFERENCES menu(id)
        )
    """.trimIndent()

    private val CREATE_REVIEW_TABLE = """
        CREATE TABLE review (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            user_id INTEGER,
            vendor_id INTEGER,
            text TEXT,
            rating REAL,
            FOREIGN KEY(vendor_id) REFERENCES vendor(id),
            FOREIGN KEY(user_id) REFERENCES user(id)
        )
    """.trimIndent()

    private val CREATE_SCANS_TABLE = """
    CREATE TABLE scans (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        vendor_id INTEGER,
        scan_time DATETIME,
        FOREIGN KEY(vendor_id) REFERENCES vendor(id)
    )
""".trimIndent()

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades
        // You might want to preserve existing data during upgrades
    }
}



