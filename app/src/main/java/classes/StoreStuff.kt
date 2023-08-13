package classes

import java.sql.Time
//this one holds all classes linked to a store
//Store, Menu, MenuItem, DietaryReq

class Store(
    val name: String,
    val cuisine: String,
    val menu: Menu,
    var rating: Int, //made a change to class diagram, could be double?
    var openTime: Time,
    var closeTime: Time,
    var dietaryReqs: DietaryReq,
    var reviewList: ArrayList<Review>,
)
{

    // I actually think calc average rating method should be in the store class not the review class
}


class Menu() //needs a list of menu items
{
    //fun addItem(var item: MenuItem){}

    //fun removeItem(var item: MenuItem)
}

class MenuItem(
    var name: String,
    var price: Double,
    var description: String,
    var inStock: Boolean,
)

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