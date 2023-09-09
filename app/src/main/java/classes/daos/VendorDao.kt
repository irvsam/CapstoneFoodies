package classes.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
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

    @Query("SELECT AVG(overAllRating) FROM review WHERE vendor_id = :vendorId")
    fun calculateAverageRating(vendorId: Long): Float

    @Transaction
    suspend fun updateVendorAverageRating(vendorId: Long) {
        val averageRating = calculateAverageRating(vendorId)
        updateAverageRating(vendorId, averageRating.toString())
    }

    @Query("UPDATE vendor SET rating = :averageRating WHERE id = :vendorId")
    suspend fun updateAverageRating(vendorId: Long, averageRating: String)

}