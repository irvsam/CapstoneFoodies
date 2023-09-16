package classes

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import classes.daos.AccountDao
import com.example.foodies.ApplicationCore

class UserRepository() {

    private val userLiveData = MutableLiveData<Entities.User?>()
    private val accountDao: AccountDao = ApplicationCore.database.accountDao()

    suspend fun updateUserRewardPoints(userId: Long, rewardPointsToAdd: Int) {
        val user = ApplicationCore.database.accountDao().getUserById(userId)
        if (user != null) {
            user.rewardPoints += rewardPointsToAdd
            user.totalOverAllPoints += rewardPointsToAdd
            ApplicationCore.database.accountDao().updateUser(user)
        }
    }
    suspend fun updateUserVoucher(userId: Long, voucherCode: String) {
        val user = ApplicationCore.database.accountDao().getUserById(userId)
        if (user != null) {
            ApplicationCore.database.accountDao().setUserVoucher(user.id,voucherCode)
        }
    }



    fun getUserLiveData(): LiveData<Entities.User?> {
        return userLiveData
    }

    // Function to get user's reward points by user ID
    suspend fun getUserRewardPoints(userId: Long): Int {
        return accountDao.getUserRewardPoints(userId)
    }

    suspend fun getUserTotalPoints(userId: Long): Int {
        return accountDao.getUserOverallPoints(userId)
    }

    suspend fun getUserVoucher(userId:Long): String?{
        return accountDao.getUserVoucher(userId)
    }

    suspend fun resetPoints(userId: Long) {
        val user = ApplicationCore.database.accountDao().getUserById(userId)
        if (user != null) {
            user.rewardPoints = 0
            ApplicationCore.database.accountDao().updateUser(user)
        }
    }
}