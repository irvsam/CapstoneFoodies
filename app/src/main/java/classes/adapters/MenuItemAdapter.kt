package classes.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.foodies.databaseManagement.ApplicationCore
import classes.Entities
import classes.VendorManagementViewModel
import com.example.foodies.ConfirmDeleteDialogFragment
import com.example.foodies.EditItemFragment
import com.example.foodies.R
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MenuItemAdapter(private val fragmentManager:FragmentManager, private val vendorManagementViewModel: VendorManagementViewModel, var menuItemList: MutableList<Entities.MenuItem?>?, private val lifecycleOwner: LifecycleOwner): RecyclerView.Adapter<MenuItemAdapter.MyViewHolder>() {

    /** Returns a ViewHolder object for each menuItem in the Stores RecyclerView*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.menu_item_card_cell,parent,false)
        return MyViewHolder(itemView)
    }

    /** Returns how many items are in the RecyclerView */
    override fun getItemCount(): Int {
        return menuItemList!!.size
    }

    /**
    Cycles through RecyclerView items and uses populateMenu() method to assign the menuItem template with the appropriate details of the item
     */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = menuItemList!![position]
        populateMenu(holder,currentItem)
    }

    /**
     * Takes the currently viewed menu item and binds details regarding the name and stock availability of the item. OnClickListeners are set up for the following actions:
     * delete, set availability, and edit item details
     */
    private fun populateMenu(holder:MyViewHolder,currentItem: Entities.MenuItem?){
        if (currentItem != null) {
            if (!currentItem.inStock) {
                holder.availabilityButton.setBackgroundResource(R.drawable.circle_default)
            }
            else {
                holder.availabilityButton.setBackgroundResource(R.drawable.circle_pressed)
            }

            holder.deleteButton.setOnClickListener{
                val confirmation = ConfirmDeleteDialogFragment(currentItem,lifecycleOwner)
                confirmation.show(fragmentManager,"Delete")
            }

            holder.editCardView.setOnLongClickListener{view->
                val editFragment= EditItemFragment(currentItem.id)
                val context = view.context
                val fragmentManager = (context as AppCompatActivity).supportFragmentManager
                editFragment.show(fragmentManager,"EditItemDialog")
                true
            }

            holder.availabilityButton.setOnClickListener{
                lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    if (!currentItem.inStock) {
                        holder.availabilityButton.setBackgroundResource(R.drawable.circle_pressed)
                        currentItem.inStock = true
                    }
                    else {
                        holder.availabilityButton.setBackgroundResource(R.drawable.circle_default)
                        currentItem.inStock = false
                    }
                    ApplicationCore.database.menuItemDao().updateMenuItem(currentItem)
                }
            }
            holder.menuItem.text = currentItem.name
        }
    }

    /** Assigns the attributes that the ViewHolder will have */
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val menuItem: TextView = itemView.findViewById(R.id.menuItemName)
        val deleteButton: ImageButton = itemView.findViewById(R.id.delete)
        val availabilityButton: ImageButton = itemView.findViewById(R.id.stock)
        val editCardView: MaterialCardView = itemView.findViewById(R.id.menuItemCardView)
    }
}