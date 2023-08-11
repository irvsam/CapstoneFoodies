//this one holds all classes linked to a store


class Store(
    val name: String,
    val cuisine: String,
    val menu: Menu,
    var rating: Int, //made a change to class diagram, could be double?
    var openTime: Time, // dont know about Time objects
    var closeTime: Time,
    var dietaryReqs: DietaryRequirements,
    var reviewList: ArrayList<Review> ,  //this must be an array of review objects
)
{

    // I actually think calc average rating method should be in the store class not the review class
}


class Menu() //needs a list of menu items
{
    fun addItem(var item: MenuItem){
    }

    fun removeItem(var item:MenuItem)

}

class MenuItem(
    var name: String,
    var price: Double,
    var description: String,
    var inStock: Boolean,
)

enum class DietaryReq(val requirement: String){
    Vegetarian("Vegetarian"),
    Vegan("Vegan"),
    Pescatarian("Pescatarian"),
    Lactose-free("Lactose-free"),
    Gluten-free("Gluten-free"),
    Nut-free("Nut-free"),
    Halaal("Halaal"),
    Kosher("Kosher"),
}