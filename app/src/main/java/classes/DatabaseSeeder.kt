package classes

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Class to populate tables with preloaded data: vendor details, dietReq's etc.
 */
object DatabaseSeeder {
    suspend fun seedMenuTable(database: AppDatabase, entitiesToSeed: List<Entities.Menu>) {
        withContext(Dispatchers.IO) {
            // run through the passed list of entities to insert
            for (entity in entitiesToSeed) {
                // Query the menu table to see if the menuId that's being inserted already exists
                val existingMenu = database.menuDao().getMenuById(entity.id)
                if(existingMenu == null) {
                    insertMenu(database, entity)
                }
            }
        }
    }

    suspend fun seedMenuItemTable(database: AppDatabase,entitiesToSeed: List<Entities.MenuItem>){
        withContext(Dispatchers.IO){
            for (entity in entitiesToSeed){
                val existingMenuItem = database.menuItemDao().getMenuItemById(entity.id)
                if(existingMenuItem==null){
                    insertMenuItem(database,entity)
                }
            }
        }
    }

    suspend fun seedDietaryReqTable(database: AppDatabase, entitiesToSeed: List<Entities.DietaryRequirement>) {
        withContext(Dispatchers.IO) {
            for (entity in entitiesToSeed) {
                // Query the dietary_req table to see if the dietary_req to be inserted already exists
                val existingDietaryReq = database.dietaryReqDao().getDietaryReqById(entity.id)
                if(existingDietaryReq == null){
                    insertDietaryRequirement(database, entity)
                }
            }
        }
    }

    suspend fun seedVendorTable(database: AppDatabase, entitiesToSeed: List<Entities.Vendor>) {
        withContext(Dispatchers.IO) {
            for (entity in entitiesToSeed) {
                val existingVendor = database.vendorDao().getVendorById(entity.id)
                if(existingVendor == null) {
                  insertVendor(database, entity)
                }
            }
        }
    }

    // Insert functions for each table
    private suspend fun insertMenu(database: AppDatabase, menu: Entities.Menu) {
        withContext(Dispatchers.IO) {
            database.menuDao().insertMenu(menu)
        }
    }

    private suspend fun insertMenuItem(database: AppDatabase,menuItem: Entities.MenuItem){
        withContext(Dispatchers.IO){
            database.menuItemDao().insertMenuItem(menuItem)
        }
    }

    private suspend fun insertDietaryRequirement(database: AppDatabase, dietaryReq: Entities.DietaryRequirement) {
        withContext(Dispatchers.IO) {
            database.dietaryReqDao().insertDietaryRequirement(dietaryReq)
        }
    }

    private suspend fun insertVendor(database: AppDatabase, vendor: Entities.Vendor) {
        withContext(Dispatchers.IO) {
            database.vendorDao().insertVendor(vendor)
        }
    }
}
