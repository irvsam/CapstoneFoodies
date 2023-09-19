package classes.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import classes.Entities
import java.io.Serializable

@Dao
interface VendorDao {

    @Insert
    suspend fun insertVendor(vendor: Entities.Vendor)
    @Update
    suspend fun updateVendor(vendor: Entities.Vendor)
    @Query("SELECT * FROM vendor WHERE id = :id")
    suspend fun getVendorById(id: Long): Entities.Vendor?

    @Query("SELECT * FROM vendor")
    suspend fun getAllVendors(): List<Entities.Vendor?>

    @Query("SELECT * FROM menuitem WHERE menu_id = :id")
    suspend fun getMenuItemsByMenuId(id: Long?): List<Entities.MenuItem?>

    @Query("SELECT ROUND(AVG(overAllRating), 1) FROM review WHERE vendor_id = :vendorId")
    fun calculateAverageRating(vendorId: Long): Float?

    @Transaction
    suspend fun updateVendorAverageRating(vendorId: Long) {
        val averageRating = calculateAverageRating(vendorId)
        updateAverageRating(vendorId, averageRating)
    }

    @Query("UPDATE vendor SET rating = :averageRating WHERE id = :vendorId")
    suspend fun updateAverageRating(vendorId: Long, averageRating: Float?)

    @Query("SELECT COUNT(*) FROM review WHERE vendor_id = :vendorId")
    suspend fun getReviewCountForVendor(vendorId: Long): Int

    @Query("SELECT name FROM vendor")
    suspend fun getAllVendorNames(): List<String>

}