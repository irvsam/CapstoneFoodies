package com.example.foodies

import android.app.Application
import androidx.room.Room
import classes.AppDatabase

class ApplicationCore : Application() {

    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app-database")
            .build()
        
    }
}