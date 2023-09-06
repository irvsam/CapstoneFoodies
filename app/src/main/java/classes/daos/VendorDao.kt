package classes.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import classes.Entities

@Dao
interface VendorDao {

    @Insert
    suspend fun insertVendor(vendor: Entities.Vendor)

    @Query("SELECT * FROM vendor WHERE id = :id")
    suspend fun getVendorById(id: Long): Entities.Vendor?

}