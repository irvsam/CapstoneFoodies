package classes

//this holds all the shared view models needed

import androidx.lifecycle.ViewModel
import classes.Store
//store list view model
class SharedViewModel : ViewModel() {
    val storeList = mutableListOf<Entities.Vendor?>()
}
// this is to store user details
class UserViewModel : ViewModel() {
    var user: Entities.User? = null
}

class GuestViewModel : ViewModel() {
    var isGuest: Boolean = false
}

class VendorViewModel : ViewModel(){
    var vendor: Entities.Vendor? = null
}