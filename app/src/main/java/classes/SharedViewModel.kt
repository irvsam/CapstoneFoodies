package classes

//this holds all the shared view models needed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodies.databaseManagement.ApplicationCore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//store list view model
class SharedViewModel : ViewModel() {
    var storeList = mutableListOf<Entities.Vendor?>()

    suspend fun getStores(){
        val allStores = ApplicationCore.database.vendorDao().getAllVendors()
        storeList = allStores.toMutableList()
    }
}
// this is to store account details
class AccountViewModel : ViewModel() {

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
    fun updateUserDetails(user: Entities.User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateUser(user)
        }
    }

    fun loadUserInitialVoucher(userId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val initialVoucher = userRepository.getUserVoucher(userId)
            _userVoucher.postValue(initialVoucher)
        }
    }


}

//handling guest actions
class GuestViewModel : ViewModel() {
    var isGuest: Boolean = false
}


//view model to handle all the stores for the vendor list
class StoreViewModel : ViewModel(){
    var vendor: Entities.Vendor? = null
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
    var fromVendorList: Boolean = false
    var fromManagementPage: Boolean = false
}

//this is for handling the logged on vendor
class VendorManagementViewModel: ViewModel(){
    private val vendorRepository: VendorRepository = VendorRepository()
    private val _ratingLiveData = MutableLiveData<Float?>()
    private val _isLoading = MutableLiveData<Boolean>()
    var isVendor: Boolean = false
    var user: Entities.User? = null //this will be the vendor user who is logged on
    var vendor: Entities.Vendor? = null
    val _menuItems = MutableLiveData<MutableList<Entities.MenuItem?>>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading
    val ratingLiveData: LiveData<Float?>
        get() = _ratingLiveData

    val menuItems: LiveData<MutableList<Entities.MenuItem?>>
        get() = _menuItems

    init {
        _menuItems.value = mutableListOf()
    }
    fun loadVendorInitialRating(vendorId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val initialVendorRating = vendorRepository.getVendorRating(vendorId)
            _ratingLiveData.postValue(initialVendorRating)
        }
    }

    fun updateUserDetails(user: Entities.User) {
        viewModelScope.launch(Dispatchers.IO) {
            ApplicationCore.database.accountDao().updateUser(user)
        }
    }

    fun updateVendorDetails(vendor: Entities.Vendor) {
        viewModelScope.launch(Dispatchers.IO) {
            ApplicationCore.database.vendorDao().updateVendor(vendor)
        }
    }

    fun setMenuItems(menuItemsList:MutableList<Entities.MenuItem?> ){
        val currentList = _menuItems.value ?: mutableListOf()
        for(item in menuItemsList){
            currentList.add(item)
        }
        _menuItems.value = currentList
    }

    fun addMenuItem(item:Entities.MenuItem?){
        val currentList = _menuItems.value ?: mutableListOf()
        currentList.add(item)
        _menuItems.value = currentList
    }

    fun swapEditMenuItem(orginalItem:Entities.MenuItem?,editedItem:Entities.MenuItem?){
        val currentList = _menuItems.value ?: mutableListOf()
        val newList:MutableList<Entities.MenuItem?>? = currentList.map{if (it==orginalItem) editedItem else it}.toMutableList()
        _menuItems.value = newList
    }

    fun deleteItem(item:Entities.MenuItem?){
        val currentList = _menuItems.value ?: mutableListOf()
        currentList.remove(item)
        _menuItems.postValue(currentList)
    }

    fun updateRating(rating: Float?) {
        _ratingLiveData.postValue(rating)
    }

    suspend fun setVendor() {
        _isLoading.postValue(true)
        vendor = ApplicationCore.database.accountDao().getVendorStore(user?.vendorId)
        _isLoading.postValue(false)
    }

    suspend fun setMenu(){
        _isLoading.postValue(true)
        menuItems = ApplicationCore.database.menuItemDao().getMenuItemsByMenuId(vendor!!.menuId)
        _isLoading.postValue(false)
    }


}