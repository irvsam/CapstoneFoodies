package classes.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import classes.Entities

@Dao
interface DietaryReqDao {

    @Insert
    suspend fun insertDietaryRequirement(dietaryReq: Entities.DietaryRequirement)

    @Query("SELECT * FROM dietary_req WHERE id = :id")
    suspend fun getDietaryReqById(id: Long): Entities.DietaryRequirement?
}