package dao

import androidx.room.Dao
import androidx.room.Insert
import classes.Entities

@Dao
interface AccountDao {
    // DAO methods here
    @Insert
    suspend fun insertUser(user: Entities.User)

    @Insert
    suspend fun insertVendor(vendor: Entities.Vendor)


}