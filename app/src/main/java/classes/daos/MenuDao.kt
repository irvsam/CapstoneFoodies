package classes.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import classes.Entities

@Dao
interface MenuDao {

    /** Insert a Menu item the Menu table*/
    @Insert
    suspend fun insertMenu(menu: Entities.Menu)

    /** Returns all menus with the corresponding ID*/
    @Query("SELECT * FROM menu WHERE id = :id")
    suspend fun getMenuById(id: Long): Entities.Menu?

}