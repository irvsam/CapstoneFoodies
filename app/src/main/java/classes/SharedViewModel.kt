package classes

//this holds all the shared view models needed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import classes.Store
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//store list view model
class SharedViewModel : ViewModel() {
    val storeList = mutableListOf<Entities.Vendor?>()
}
// this is to store user details
class UserViewModel : ViewModel() {
    var user: Entities.User? = null
    private val userRepository: UserRepository = UserRepository()

    val userLiveData: LiveData<Entities.User?> = userRepository.getUserLiveData()
    fun updateUserRewardPoints(userId: Long, rewardPointsToAdd: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateUserRewardPoints(userId, rewardPointsToAdd)
        }
    }
}

class GuestViewModel : ViewModel() {
    var isGuest: Boolean = false
}

class VendorViewModel : ViewModel(){
    var vendor: Entities.Vendor? = null
}