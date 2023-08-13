package classes

import java.util.ArrayList

// classes relating to accounts
// Account, UserAccount, VendorAccount

open class Account(
    //account class for creating accounts and storing account details
    //user and vendors will inherit from here
    var userName: String,
    var password: String,
    var email: String,
    var phoneNo: String,
    var loggedIn: Boolean,
)

{
    fun register() {
        //create a new account
    }

    fun login(){
        //log in using username and password
    }



}

class UserAccount(
    //inherits from account with extra user details

    userName: String,
    password: String,
    email: String,
    phoneNo: String,
    loggedIn: Boolean,
    uctID: Double,
    reviewHistory: ArrayList<Review>,
    rewardPoints: RewardSystem


) : Account(userName, password, email, phoneNo, loggedIn)
{
    fun leaveReview(storeName: String) {
        //create a review for the given store


    }
}

class VendorAccount(
    //inherits from account with extra vendor details

    userName: String,
    password: String,
    email: String,
    phoneNo: String,
    loggedIn: Boolean,

) : Account(userName, password, email, phoneNo, loggedIn)

