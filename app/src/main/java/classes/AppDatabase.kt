package classes

import androidx.room.*

@Database(
    entities = [Entities.User::class,
    Entities.Vendor::class,
    Entities.DietaryRequirement::class,
    Entities.MenuItem::class,
    Entities.Menu::class,
    Entities.Review::class],
    version = 2, exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao


}