package classes

import androidx.room.*
import classes.daos.AccountDao
import androidx.lifecycle.lifecycleScope
import classes.daos.DietaryReqDao
import classes.daos.MenuDao
import classes.daos.VendorDao
import com.example.foodies.ApplicationCore

@Database(
    entities = [Entities.User::class,
    Entities.Vendor::class,
    Entities.DietaryRequirement::class,
    Entities.MenuItem::class,
    Entities.Menu::class,
    Entities.Review::class],
    version = 3, exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao
    abstract fun reviewDao(): ReviewDao

    abstract fun vendorDao(): VendorDao

    abstract fun dietaryReqDao(): DietaryReqDao

    abstract fun menuDao(): MenuDao

}