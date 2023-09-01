package classes

import androidx.room.*


class Entities {

    // USER
    @Entity(tableName = "user")
    data class User(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val email: String,
        val password: String,
        val type: String,
        val rewardPoints: Int
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
        val rating: Float,
        @ColumnInfo(name = "openTime") val openTime: String,
        @ColumnInfo(name = "closeTime") val closeTime: String,
        @ColumnInfo(name = "dietaryReq_id") val dietaryReqId: Long
    )

    @Entity(tableName = "dietary_req")
    data class DietaryRequirement(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val type: String
    )

    // MENU
    @Entity(
        tableName = "menu",
        foreignKeys = [ForeignKey(
            entity = Vendor::class,
            parentColumns = ["id"],
            childColumns = ["vendor_id"],
            onDelete = ForeignKey.CASCADE
        )]
    )
    data class Menu(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        @ColumnInfo(name = "vendor_id") val vendorId: Long,
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
        val price: Float
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
        val rating: Float
    )

}