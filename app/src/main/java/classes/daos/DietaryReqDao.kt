package classes.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import classes.Entities

@Dao
interface DietaryReqDao {

    /** Inserts the dietary requirement into the dieataryReq table*/
    @Insert
    suspend fun insertDietaryRequirement(dietaryReq: Entities.DietaryRequirement)

    /** Returns the dietary requirement with the corresponding id*/
    @Query("SELECT * FROM dietary_req WHERE id = :id")
    suspend fun getDietaryReqById(id: Long): Entities.DietaryRequirement?
}