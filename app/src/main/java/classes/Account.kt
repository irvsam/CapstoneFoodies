package classes

import androidx.room.Entity
import java.time.LocalDateTime
import java.util.ArrayList

// classes relating to accounts
// Account, UserAccount, VendorAccount

open class Account(
    //account class for creating accounts and storing account details
    //user and vendors will inherit from here
    private var userName: String,
    private var password: String,
    var email: String,
    var phoneNo: String,
    private var loggedIn: Boolean, //if this is set to true then the account can have access to user functionality
)

{
    fun register(usrN: String,pswd: String,eml: String,phone: String) {
        //create a new account by just calling the constructor
        val account = Account(usrN,pswd,eml,phone,false)
    }

    fun login(usrN: String, pswd: String): String {
        //log in using username and password
        //if correct then log them in
        loggedIn = usrN==userName && pswd==password
        return if(loggedIn){
            "You have been successfully logged in!"

        } else{
            "Error, username or password incorrect"
        }
    }

    fun logout(){
        loggedIn = false

    }



}


class UserAccount(
    //inherits from account with extra user details

    userName: String,
    password: String,
    email: String,
    phoneNo: String,
    loggedIn: Boolean,
    val uctID: Double, //maybe remove
    private val reviewHistory: ArrayList<Review>,
    val rewardPoints: RewardSystem // could also just calculate based on review history??

) : Account(userName, password, email, phoneNo, loggedIn)
{


    fun leaveReview(store: Store, overallRating: Double, comment: String) {
        // Create a review for the given store and add to review history and add to store list
        val ldt = LocalDateTime.now()
        val review = Review(this, store, overallRating, comment,ldt )
        reviewHistory.add(review)
        store.reviewList.add(review)
    }
}

class VendorAccount(
    //inherits from account with extra vendor details

    userName: String,
    password: String,
    email: String,
    phoneNo: String,
    loggedIn: Boolean,
    val store: Store, //could be string store name?
    val vendorID: Double //or string?

) : Account(userName, password, email, phoneNo, loggedIn)

{fun replyToComment(){
    //method to reply to a user comment
}
}

