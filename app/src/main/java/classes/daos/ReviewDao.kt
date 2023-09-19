package classes.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import classes.Entities

@Dao
interface ReviewDao {
    @Insert
    suspend fun insertReview(review: Entities.Review)

    @Update
    suspend fun updateReview(review: Entities.Review)

    @Delete
    suspend fun deleteReview(review: Entities.Review)

    @Query("SELECT * FROM review WHERE user_id = :userId")
    suspend fun getReviewsByUserId(userId: Long): List<Entities.Review>

    @Query("SELECT * FROM review WHERE vendor_id = :vendorId")
    suspend fun getReviewsByVendorId(vendorId: Long): List<Entities.Review>

    @Query("UPDATE review SET reply =:replyString WHERE id =:reviewId")
    suspend fun updateReply(replyString:String,reviewId:Long)

    // Add other query methods as needed
}