package classes

/** this class holds all our shared view models*/

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodies.databaseManagement.ApplicationCore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** shared view model for the list of stores in vendor list*/
class SharedViewModel : ViewModel() {
    var storeList = mutableListOf<Entities.Vendor?>()

    /** set the storelist to all the stores in the database*/
    suspend fun getStores(){
        val allStores = ApplicationCore.database.vendorDao().getAllVendors()
        storeList = allStores.toMutableList()
    }
}

/** for searching vendors*/
class SearchSharedViewModel : ViewModel(){
    private val _searchStoreList = MutableLiveData<MutableList<Entities.Vendor?>>()
    val searchStoreList: LiveData<MutableList<Entities.Vendor?>>
        get() = _searchStoreList

    init {
        _searchStoreList.value = mutableListOf()
    }
    fun addStoreToSearch(store: Entities.Vendor?){
        val currentList = _searchStoreList.value ?: mutableListOf()
        currentList.add(store)
        _searchStoreList.value=currentList
    }
}


/** holds all information for currently logged in account */
class AccountViewModel : ViewModel() {

    var user: Entities.User? = null
    private val userRepository: UserRepository = UserRepository()

    // for live data updates */
    private val _userRewardPoints = MutableLiveData<Int>()
    private val _userTotalPoints = MutableLiveData<Int>()

    private val _userVoucher = MutableLiveData<String?>()
    val userVoucher: LiveData<String?>
        get() = _userVoucher

    val userRewardPoints: LiveData<Int>
        get() = _userRewardPoints

    val userTotalPoints: LiveData<Int>
        get() = _userTotalPoints

    /** set the user's voucher */
    fun updateUserVoucher(userId: Long, newVoucherCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateUserVoucher(userId, newVoucherCode)
            // post to the live data and set this user*/
            _userVoucher.postValue(newVoucherCode)
            user?.currentVoucher = newVoucherCode
        }
    }

    fun updateUserRewardPoints(userId: Long, rewardPointsToAdd: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            //calculate the new reward points and set */
            userRepository.updateUserRewardPoints(userId, rewardPointsToAdd)
            val updatedRewardPoints = _userRewardPoints.value?.plus(rewardPointsToAdd) ?: rewardPointsToAdd
            val updatedTotalPoints = _userTotalPoints.value?.plus(rewardPointsToAdd) ?: rewardPointsToAdd
            _userRewardPoints.postValue(updatedRewardPoints)
            _userTotalPoints.postValue(updatedTotalPoints)
            user?.rewardPoints = updatedRewardPoints
            user?.totalOverAllPoints = updatedTotalPoints
        }
    }

    /** set to 0 when they take out their voucher*/
    fun resetUserRewardPoints(userId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.resetPoints(userId)
            val updatedRewardPoints = 0
            _userRewardPoints.postValue(updatedRewardPoints)
            user?.rewardPoints = 0
        }
    }

    /** load from database for initial live data value */
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

/** this keeps track of if the current user is a guest*/
class GuestViewModel : ViewModel() {
    var isGuest: Boolean = false
}


/** keep track of the stores data */
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

    fun loadDescription(vendorId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            val description = vendorRepository.getDescription(vendorId)
            vendor?.description  = description
        }
    }

    fun updateRating(rating: Float?) {
            _ratingLiveData.postValue(rating)
    }
}

/** keep track of the reviews for the review list */
class ReviewViewModel: ViewModel(){
    var fromVendorList: Boolean = false
    var fromManagementPage: Boolean = false
    val _reviewList = MutableLiveData<MutableList<Entities.Review?>>()

    val reviewList: LiveData<MutableList<Entities.Review?>>
        get() = _reviewList

    init {
        _reviewList.value = mutableListOf()
    }

    fun addReview(review: Entities.Review) {
        val currentList = _reviewList.value ?: mutableListOf()
        currentList.add(review)
        _reviewList.value = currentList
    }

    fun clearReviews() {
        _reviewList.value = mutableListOf()
    }

    fun updateReviewList(reviewOutdated: Entities.Review?,reviewUpdated: Entities.Review?){
        val currentList = _reviewList.value ?: mutableListOf()
        val newList:MutableList<Entities.Review?>? = currentList.map{if (it==reviewOutdated) reviewUpdated else it}.toMutableList()
        _reviewList.value = newList
    }

}

/** keep track of a vendor account, if they are logged in as a vendor user */
class VendorManagementViewModel: ViewModel(){
    private val vendorRepository: VendorRepository = VendorRepository()
    private val _ratingLiveData = MutableLiveData<Float?>()
    private val _isLoading = MutableLiveData<Boolean>() // very useful: tells us if the data is still being retrieved from database*/
    var isVendor: Boolean = false
    var user: Entities.User? = null
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

    /** user "vendor" account*/
    fun updateUserDetails(user: Entities.User) {
        viewModelScope.launch(Dispatchers.IO) {
            ApplicationCore.database.accountDao().updateUser(user)
        }
    }

    /** vendor store account*/
    fun updateVendorDetails(vendor: Entities.Vendor) {
        viewModelScope.launch(Dispatchers.IO) {
            ApplicationCore.database.vendorDao().updateVendor(vendor)
        }
    }

    /** set the menu items from database */
    fun setMenuItems(menuItemsList:MutableList<Entities.MenuItem?> ){
        val currentList = _menuItems.value ?: mutableListOf()
        for(item in menuItemsList){
            currentList.add(item)
        }
        _menuItems.value = currentList
    }
    // the following methods are for menu management*/
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

    suspend fun setVendor() {
        _isLoading.postValue(true)
        vendor = ApplicationCore.database.accountDao().getVendorStore(user?.vendorId)
        _isLoading.postValue(false)
    }



}