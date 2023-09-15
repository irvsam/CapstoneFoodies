package classes.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import classes.Entities

@Dao
interface AccountDao {
    // DAO methods here
    @Insert
    suspend fun insertUser(user: Entities.User)
    @Insert
    fun insertVendor(vendor: Entities.Vendor)
    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    suspend fun getUserByEmailAndPassword(email: String, password: String): Entities.User?

    @Query("SELECT vendor.* FROM vendor INNER JOIN user ON vendor.id=user.vendorId WHERE user.vendorId=:vendorId")
    suspend fun getVendorStore(vendorId: Long?): Entities.Vendor?

    @Query("SELECT * FROM user WHERE email = :email AND username = :username")
    suspend fun getUserByEmailAndUsername(email: String, username: String): Entities.User?

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserById(id: Long): Entities.User?
    @Update
    suspend fun updateUser(user: Entities.User)

    @Query("SELECT username FROM user WHERE id = :userId")
    suspend fun getUsernameById(userId: Long): String?

    @Query("SELECT rewardPoints FROM User WHERE id = :userId")
    suspend fun getUserRewardPoints(userId: Long): Int

    @Query("SELECT totalOverAllPoints FROM User WHERE id = :userId")
    suspend fun getUserOverallPoints(userId: Long): Int

    @Query("SELECT COUNT(*) FROM User WHERE email = :email")
    fun getCountByEmail(email: String): Int

    @Query("SELECT COUNT(*) FROM User WHERE phone = :phone")
    fun getCountByPhone(phone: String): Int

    @Query("SELECT COUNT(*) FROM User WHERE username = :username")
    fun getCountByUsername(username: String): Int

}