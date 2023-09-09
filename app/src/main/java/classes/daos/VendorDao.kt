package classes.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import classes.Entities
import java.io.Serializable

@Dao
interface VendorDao {

    @Insert
    suspend fun insertVendor(vendor: Entities.Vendor)

    @Query("SELECT * FROM vendor WHERE id = :id")
    suspend fun getVendorById(id: Long): Entities.Vendor?

    @Query("SELECT * FROM vendor")
    suspend fun getAllVendors(): List<Entities.Vendor?>

    @Query("SELECT * FROM menuitem WHERE menu_id = :id")
    suspend fun getMenuItemsByMenuId(id: Long?): List<Entities.MenuItem?>

}