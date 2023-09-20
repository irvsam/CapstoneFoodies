package classes.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import classes.Entities

@Dao
interface AccountDao {
    // DAO methods here

    /** Insets a user into the User table*/
    @Insert
    suspend fun insertUser(user: Entities.User)

    /** Inserts a vendor into the vendor table*/
    @Insert
    fun insertVendor(vendor: Entities.Vendor)

    /** Returns all users with the corresponding email and password*/
    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    suspend fun getUserByEmailAndPassword(email: String, password: String): Entities.User?

    /** Returns all vendors that have a matching vendorId in the user and vendors tables*/
    @Query("SELECT vendor.* FROM vendor INNER JOIN user ON vendor.id=user.vendorId WHERE user.vendorId=:vendorId")
    suspend fun getVendorStore(vendorId: Long?): Entities.Vendor?

    /** Returns all users with a corresponding email and username*/
    @Query("SELECT * FROM user WHERE email = :email AND username = :username")
    suspend fun getUserByEmailAndUsername(email: String, username: String): Entities.User?

    /** Return all users with the correponding user id*/
    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserById(id: Long): Entities.User?

    /** Updates the user in the the user table*/
    @Update
    suspend fun updateUser(user: Entities.User)

    /** Returns the username with the corresponding userId*/
    @Query("SELECT username FROM user WHERE id = :userId")
    suspend fun getUsernameById(userId: Long): String?

    /** Returns the rewards points of the user with the corresponding userID*/
    @Query("SELECT rewardPoints FROM User WHERE id = :userId")
    suspend fun getUserRewardPoints(userId: Long): Int

    /** Returns the overall reward points of a user with the corresponding userID*/
    @Query("SELECT totalOverAllPoints FROM User WHERE id = :userId")
    suspend fun getUserOverallPoints(userId: Long): Int

    /** Returns the amount of users with the corresponding email address*/
    @Query("SELECT COUNT(*) FROM User WHERE email = :email")
    fun getCountByEmail(email: String): Int

    /** Returns the amount of users with the corresponding phone number*/
    @Query("SELECT COUNT(*) FROM User WHERE phone = :phone")
    fun getCountByPhone(phone: String): Int

    /** Returns the amount of users with the corresponding username*/
    @Query("SELECT COUNT(*) FROM User WHERE username = :username")
    fun getCountByUsername(username: String): Int

    /** Returns the current voucher code (null or code) for the user with the corresponding userID*/
    @Query("SELECT currentVoucher FROM User WHERE id = :userId")
    suspend fun getUserVoucher(userId: Long): String?

    /** Updates the voucher code for the user with the corresponding userID*/
    @Query("UPDATE user SET currentVoucher = :voucherCode WHERE id = :userId")
    suspend fun setUserVoucher(userId: Long, voucherCode: String)

}