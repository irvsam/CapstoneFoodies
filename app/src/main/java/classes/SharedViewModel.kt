package classes

//this holds all the shared view models needed

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private val _userRewardPoints = MutableLiveData<Int>()
    private val _userTotalPoints = MutableLiveData<Int>()

    private val _userVoucher = MutableLiveData<String?>()
    val userVoucher: LiveData<String?>
        get() = _userVoucher

    val userRewardPoints: LiveData<Int>
        get() = _userRewardPoints

    val userTotalPoints: LiveData<Int>
        get() = _userTotalPoints

    fun updateUserVoucher(userId: Long, newVoucherCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "updating in view model")
            userRepository.updateUserVoucher(userId, newVoucherCode)
            _userVoucher.postValue(newVoucherCode)
        }
    }

    fun updateUserRewardPoints(userId: Long, rewardPointsToAdd: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateUserRewardPoints(userId, rewardPointsToAdd)

            val updatedRewardPoints = _userRewardPoints.value?.plus(rewardPointsToAdd) ?: rewardPointsToAdd
            val updatedTotalPoints = _userTotalPoints.value?.plus(rewardPointsToAdd) ?: rewardPointsToAdd
            _userRewardPoints.postValue(updatedRewardPoints)
            _userTotalPoints.postValue(updatedTotalPoints)
        }
    }

    fun resetUserRewardPoints(userId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.resetPoints(userId)

            val updatedRewardPoints = 0
            _userRewardPoints.postValue(updatedRewardPoints)
        }
    }


    // Load the user's initial reward points from the database
    fun loadUserInitialRewardPoints(userId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val initialRewardPoints = userRepository.getUserRewardPoints(userId)
            val initialTotalPoints = userRepository.getUserTotalPoints(userId)
            _userRewardPoints.postValue(initialRewardPoints)
            _userTotalPoints.postValue(initialTotalPoints)
        }
    }

    fun loadUserInitialVoucher(userId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val initialVoucher = userRepository.getUserVoucher(userId)
            _userVoucher.postValue(initialVoucher)
        }
    }


}

class GuestViewModel : ViewModel() {
    var isGuest: Boolean = false
}

class VendorViewModel : ViewModel(){
    var user: Entities.User? = null
    var vendor: Entities.Vendor? = null
    var isVendor: Boolean = false
    private val vendorRepository: VendorRepository = VendorRepository()
    private val _ratingLiveData = MutableLiveData<Float?>()

    val ratingLiveData: LiveData<Float?>
        get() = _ratingLiveData

    fun loadVendorInitialRating(vendorId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val initialVendorRating = vendorRepository.getVendorRating(vendorId)
            _ratingLiveData.postValue(initialVendorRating)
        }
    }

    fun updateRating(rating: Float?) {
            _ratingLiveData.postValue(rating)
    }
}

class ReviewViewModel: ViewModel(){
    val reviewList = mutableListOf<Entities.Review?>()
}

class MenuItemViewModel: ViewModel(){
    var menuItems = mutableListOf<Entities.MenuItem?>()
}