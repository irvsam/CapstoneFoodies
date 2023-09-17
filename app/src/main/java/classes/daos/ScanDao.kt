package classes.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import classes.Entities

@Dao
interface ScanDao {
    @Insert
    suspend fun insert(scan: Entities.Scan)

    @Query("SELECT * FROM Scans")
    fun getAllScans(): LiveData<List<Entities.Scan>>

}