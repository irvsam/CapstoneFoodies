//this one holds all classes relating to accounts

class Account(
    var userName: String,
    var password: String,
    var email: String,
    var phoneNo: String,
    var loggedIn: Boolean,
)

class UserAccount:Account()

class VendorAccount:Account()

class RewardSystem(val point:Int, val conversion: Double)