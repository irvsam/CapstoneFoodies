package classes

import androidx.lifecycle.MutableLiveData
import classes.daos.VendorDao
import com.example.foodies.databaseManagement.ApplicationCore
class VendorRepository {
    //getting vendor (store) data from database using dao
    private val vendorLiveData = MutableLiveData<Entities.Vendor?>()
    private val vendorDao: VendorDao = ApplicationCore.database.vendorDao()

    fun getVendorRating(vendorId: Long): Float? {
        return vendorDao.calculateAverageRating(vendorId)
    }

    suspend fun getVendorList():ArrayList<String>{
        val vendorlist = ArrayList<String>()
        val list = vendorDao.getAllVendorNames()
        for(item in list){vendorlist.add(item)}
        return vendorlist
    }
}