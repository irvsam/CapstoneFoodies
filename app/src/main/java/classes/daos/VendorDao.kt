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

    /** Inserts vendor into vendor table*/
    @Insert
    suspend fun insertVendor(vendor: Entities.Vendor)

    /** Updates corresponding vendor in vendor table*/
    @Update
    suspend fun updateVendor(vendor: Entities.Vendor)

    /** Returns all vendors with the corresponding vendorID*/
    @Query("SELECT * FROM vendor WHERE id = :id")
    suspend fun getVendorById(id: Long): Entities.Vendor?

    /** Returns all vendors in the vendor table*/
    @Query("SELECT * FROM vendor")
    suspend fun getAllVendors(): List<Entities.Vendor?>

    /** Returns all menu items which have a corresponding menuID*/
    @Query("SELECT * FROM menuitem WHERE menu_id = :id")
    suspend fun getMenuItemsByMenuId(id: Long?): List<Entities.MenuItem?>

    /** Returns the rounded average rating for vendor with the corresponding vendorID*/
    @Query("SELECT ROUND(AVG(overAllRating), 1) FROM review WHERE vendor_id = :vendorId")
    fun calculateAverageRating(vendorId: Long): Float?


    /** Updates the average rating for the vendor with a corresponding vendorID*/
    @Transaction
    suspend fun updateVendorAverageRating(vendorId: Long) {
        val averageRating = calculateAverageRating(vendorId)
        updateAverageRating(vendorId, averageRating)
    }

    /** Updates the rating for a vendor with the corresponding vendorID*/
    @Query("UPDATE vendor SET rating = :averageRating WHERE id = :vendorId")
    suspend fun updateAverageRating(vendorId: Long, averageRating: Float?)

    /** Returns the amount of reviews a vendor, with a corresponding vendorID, has*/
    @Query("SELECT COUNT(*) FROM review WHERE vendor_id = :vendorId")
    suspend fun getReviewCountForVendor(vendorId: Long): Int
    @Query("SELECT description FROM vendor WHERE id = :vendorId")
    suspend fun getDescription(vendorId: Long): String


    /** Returns the name of a vendor*/
    @Query("SELECT name FROM vendor")
    suspend fun getAllVendorNames(): List<String>



}