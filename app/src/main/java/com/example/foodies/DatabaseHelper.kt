package com.example.foodies

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "food_review.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create your tables here
        db.execSQL(CREATE_VENDOR_TABLE)
        db.execSQL(CREATE_MENU_TABLE)
        db.execSQL(CREATE_DIETARY_REQ_TABLE)
        db.execSQL(CREATE_MENU_ITEM_TABLE)
        db.execSQL(CREATE_REVIEW_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades
        // You might want to preserve existing data during upgrades
    }

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
            FOREIGN KEY(dietaryReq_id) REFERENCES dietaryreq(id)
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
            vendor_id INTEGER,
            name TEXT,
            FOREIGN KEY(vendor_id) REFERENCES vendor(id)
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
            vendor_id INTEGER,
            text TEXT,
            rating REAL,
            FOREIGN KEY(vendor_id) REFERENCES vendor(id)
        )
    """.trimIndent()
    // Create other table creation strings (CREATE_MENU_TABLE, CREATE_DIETARY_REQ_TABLE, CREATE_REVIEW_TABLE)
}


