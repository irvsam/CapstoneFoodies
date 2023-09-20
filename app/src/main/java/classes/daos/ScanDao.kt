package classes.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import classes.Entities

@Dao
interface ScanDao {

    /** Inserts the scan into the scan table*/
    @Insert
    suspend fun insert(scan: Entities.Scan)

    /** Returns all scans in the scan table*/
    @Query("SELECT * FROM Scans")
    fun getAllScans(): LiveData<List<Entities.Scan>>

    /** Returns all scans with the corresponding vendorId*/
    @Query("SELECT * FROM Scans WHERE vendorId = :vendorId")
    fun getScansByVendor(vendorId: Long): List<Entities.Scan>

}