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
        val createVendorTable = """
            CREATE TABLE vendor (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                address TEXT,
                phone TEXT
            )
        """.trimIndent()
        db.execSQL(createVendorTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades
        // You might want to preserve existing data during upgrades
    }
}
