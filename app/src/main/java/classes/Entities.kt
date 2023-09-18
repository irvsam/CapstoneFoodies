package classes

import android.media.Image
import androidx.core.location.LocationRequestCompat.Quality
import androidx.room.*
import io.reactivex.internal.observers.BlockingLastObserver
import java.io.Serializable
import java.sql.Blob


class Entities {
    //here we store all the entities for our database

    // USER
    @Entity(tableName = "user")
    data class User(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        var username: String,
        val email: String,
        var phone: String,
        val password: String,
        val type: String,
        val vendorId: Long?,
        var rewardPoints: Int,
        var totalOverAllPoints: Int,
        var currentVoucher: String? = null
    )

    // VENDOR
    @Entity(
        tableName = "vendor",
        foreignKeys = [
            ForeignKey(
                entity = Menu::class,
                parentColumns = ["id"],
                childColumns = ["menu_id"],
                onDelete = ForeignKey.CASCADE
            ),
            ForeignKey(
                entity = DietaryRequirement::class,
                parentColumns = ["id"],
                childColumns = ["dietaryReq_id"],
                onDelete = ForeignKey.CASCADE
            )
        ]
    )
    data class Vendor(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val name: String,
        val cuisine: String,
        @ColumnInfo(name = "menu_id") val menuId: Long,
        val rating: String = "no reviews yet",
        @ColumnInfo(name = "openTime") val openTime: String,
        @ColumnInfo(name = "closeTime") val closeTime: String,
        @ColumnInfo(name = "dietaryReq_id") val dietaryReqId: Long,
        val image: Int,
    ) : Serializable

    @Entity(tableName = "dietary_req")
    data class DietaryRequirement(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val type: String
    )

    // MENU
    @Entity(
        tableName = "menu"
    )
    data class Menu(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val name: String
    )

    // MENU_ITEM
    @Entity(
        tableName = "menuitem",
        foreignKeys = [
            ForeignKey(
                entity = Menu::class,
                parentColumns = ["id"],
                childColumns = ["menu_id"],
                onDelete = ForeignKey.CASCADE
            )
        ]
    )

    data class MenuItem(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        @ColumnInfo(name = "menu_id") val menuId: Long,
        val name: String,
        val price: Float,
        var inStock: Boolean
    )

    // REVIEW
    @Entity(
        tableName = "review",
        foreignKeys = [
            ForeignKey(
                entity = User::class,
                parentColumns = ["id"],
                childColumns = ["user_id"],
                onDelete = ForeignKey.CASCADE
            ),
            ForeignKey(
                entity = Vendor::class,
                parentColumns = ["id"],
                childColumns = ["vendor_id"],
                onDelete = ForeignKey.CASCADE
            )
        ]
    )
    data class Review(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        @ColumnInfo(name = "user_id") val userId: Long,
        @ColumnInfo(name = "vendor_id") val vendorId: Long,
        val text: String,
        val timestamp: Long,
        val overAllRating: Float,
        val quality: Float,
        val cleanliness: Float,
        val friendliness: Float,
        val efficiency: Float,
    )

    @Entity(
        tableName = "Scans",
        foreignKeys = [
            ForeignKey(
                entity = Vendor::class,
                parentColumns = ["id"],
                childColumns = ["vendorId"],
                onDelete = ForeignKey.CASCADE
            )
        ]
    )
    data class Scan(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val vendorId: Long, // Foreign key referencing Vendor's id
        val hour: Int // Store the day of the week as an integer
    )

}
