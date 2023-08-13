package classes

//this one holds all classes relating to accounts

open class Account(
    var userName: String,
    var password: String,
    var email: String,
    var phoneNo: String,
    var loggedIn: Boolean,
)

class UserAccount

class VendorAccount

class RewardSystem(val point:Int, val conversion: Double)