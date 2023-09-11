package classes

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import classes.daos.AccountDao
import classes.daos.VendorDao
import com.example.foodies.ApplicationCore
class VendorRepository {

    private val vendorLiveData = MutableLiveData<Entities.Vendor?>()
    private val vendorDao: VendorDao = ApplicationCore.database.vendorDao()

    suspend fun getVendorRating(vendorId: Long): String {
        return vendorDao.calculateAverageRating(vendorId).toString()
    }
}