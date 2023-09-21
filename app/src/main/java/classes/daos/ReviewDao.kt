package classes.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import classes.Entities

@Dao
interface ReviewDao {

    /** Inserts a review into the review table*/
    @Insert
    suspend fun insertReview(review: Entities.Review)

    /** Updates the corresponding review*/
    @Update
    suspend fun updateReview(review: Entities.Review)

    /** Removes the corresponding review from the review DB table*/
    @Delete
    suspend fun deleteReview(review: Entities.Review)

    /** Returns all reviews corresponding to the vendor_id*/
    @Query("SELECT * FROM review WHERE vendor_id = :vendorId")
    suspend fun getReviewsByVendorId(vendorId: Long): List<Entities.Review>

    @Query("UPDATE review SET reply =:replyString WHERE id =:reviewId")
    suspend fun updateReply(replyString:String,reviewId:Long)

    // Add other query methods as needed
}