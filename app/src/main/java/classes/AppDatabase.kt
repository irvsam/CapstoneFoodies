package classes

import androidx.room.*

@Database(entities = [Entities.User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    // abstract fun userDao(): UserDao
}