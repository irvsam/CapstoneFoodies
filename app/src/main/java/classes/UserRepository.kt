package classes

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import classes.daos.AccountDao
import com.example.foodies.ApplicationCore

class UserRepository() {

    private val userLiveData = MutableLiveData<Entities.User?>()

    suspend fun updateUserRewardPoints(userId: Long, rewardPointsToAdd: Int) {
        val user = ApplicationCore.database.accountDao().getUserById(userId)
        if (user != null) {
            user.rewardPoints += rewardPointsToAdd
            ApplicationCore.database.accountDao().updateUser(user)
        }
    }

    fun getUserLiveData(): LiveData<Entities.User?> {
        return userLiveData
    }
}