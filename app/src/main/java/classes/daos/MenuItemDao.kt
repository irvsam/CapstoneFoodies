package classes.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import classes.Entities

@Dao
interface MenuItemDao {

    /** Inserts a MenuItem into the menuItem table*/
    @Insert
    suspend fun insertMenuItem(menuItem: Entities.MenuItem)

    /** Returns all menu items with the corresponding ID*/
    @Query("SELECT * FROM menuItem WHERE id = :id")
    suspend fun getMenuItemById(id: Long): Entities.MenuItem?

    /** Returns all menu items that belong to a specific menuID*/
    @Query("SELECT * FROM menuitem WHERE menu_id = :menuId")
    suspend fun getMenuItemsByMenuId(menuId: Long):MutableList<Entities.MenuItem?>

    /** Returns the last menu item ID in the database*/
    @Query("SELECT * FROM menuitem ORDER BY id DESC LIMIT 1")
    suspend fun getLastMenuItem(): Entities.MenuItem

    /** Removes the menu item from the database*/
    @Delete
    suspend fun deleteItem(menuItem:Entities.MenuItem?)

    /** Updates the corresponding menu item*/
    @Update
    suspend fun updateMenuItem(menuItem: Entities.MenuItem)
}