package classes

import androidx.room.*
import classes.daos.AccountDao
import classes.daos.DietaryReqDao
import classes.daos.MenuDao
import classes.daos.ReviewDao
import classes.daos.VendorDao

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