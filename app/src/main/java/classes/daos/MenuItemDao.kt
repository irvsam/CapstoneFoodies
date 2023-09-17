package classes.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import classes.Entities

@Dao
interface MenuItemDao {
    @Insert
    suspend fun insertMenuItem(menuItem: Entities.MenuItem)

    @Query("SELECT * FROM menuItem WHERE id = :id")
    suspend fun getMenuItemById(id: Long): Entities.MenuItem?

    @Query("SELECT * FROM menuitem")
    suspend fun getAllMenuItems(): List<Entities.MenuItem?>

    @Query("SELECT * FROM menuitem WHERE menu_id = :menuId")
    suspend fun getMenuItemsByMenuId(menuId: Long):MutableList<Entities.MenuItem?>

    @Query("SELECT * FROM menuitem ORDER BY id DESC LIMIT 1")
    suspend fun getLastMenuItem(): Entities.MenuItem

    @Delete
    suspend fun deleteItem(menuItem:Entities.MenuItem?)

    @Update
    suspend fun updateMenuItem(menuItem: Entities.MenuItem)
}