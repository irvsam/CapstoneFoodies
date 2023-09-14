package classes

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import classes.daos.AccountDao
import classes.daos.VendorDao
import com.example.foodies.ApplicationCore
class VendorRepository {

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