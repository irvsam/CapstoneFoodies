package classes

import java.sql.Time
//this one holds all classes linked to a store
//Store, Menu, MenuItem, DietaryReq

class Store(

    val name: String,
    val cuisine: String,
    val menu: Menu,
    private var rating: Double, //made a change to class diagram, could be double?
    var openTime: Time,
    var closeTime: Time,
    var dietaryReqs: DietaryReq,
    var reviewList: ArrayList<Review>,
)
{
    fun calcAveRating() {
        if (reviewList.isNotEmpty()) {
            // Calculate the sum of all ratings
            var totalRating: Double = 0.0
            for(review in reviewList){
                totalRating += review.overAllRating
            }

            // Calculate the average rating
            val averageRating = totalRating/ reviewList.size
            // Update the store's rating
            rating = averageRating
        } else {
            // If there are no reviews, what do we set the rating to??

        }
    }
}


class Menu(
    private var menu: ArrayList<MenuItem>
)
{
    fun addItem(item: MenuItem) {
        // Add an item to the menu
        menu.add(item)}

    fun removeItem(item: MenuItem) {
        // Remove an item from the menu
        menu.remove(item)
    }

}

class MenuItem(
    var name: String,
    var price: Double,
    var description: String,
    var inStock: Boolean,
){
    fun setStock(boolean: Boolean){
        inStock=boolean
    }
}

enum class DietaryReq(val requirement: String){
    VEGETARIAN("Vegetarian"),
    VEGAN("Vegan"),
    PESCATARIAN("Pescatarian"),
    LACTOSE_FREE("Lactose-free"),
    GLUTEN_FREE("Gluten-free"),
    NUT_FREE("Nut-free"),
    HALAL("Halal"),
    KOSHER("Kosher"),
}