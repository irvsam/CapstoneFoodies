package classes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import classes.daos.AccountDao
import com.example.foodies.databaseManagement.ApplicationCore

/** a repository to keep all user actions in one place and interact with the database effectively */
class UserRepository() {
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

    suspend fun updateUser(user: Entities.User) {
        ApplicationCore.database.accountDao().updateUser(user)
    }

    suspend fun getUserRewardPoints(userId: Long): Int {
        return accountDao.getUserRewardPoints(userId)
    }

    suspend fun getUserTotalPoints(userId: Long): Int {
        return accountDao.getUserOverallPoints(userId)
    }

    suspend fun getUserVoucher(userId:Long): String?{
        return accountDao.getUserVoucher(userId)
    }

    /** set to 0 when a user claims their voucher*/
    suspend fun resetPoints(userId: Long) {
        val user = ApplicationCore.database.accountDao().getUserById(userId)
        if (user != null) {
            user.rewardPoints = 0
            ApplicationCore.database.accountDao().updateUser(user)
        }
    }
}