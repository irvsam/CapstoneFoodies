package classes.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
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
    suspend fun getVendorsMenuItems(menuId: Long):List<Entities.MenuItem?>
}