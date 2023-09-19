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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.menu_item_card_cell,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return menuItemList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = menuItemList!![position]
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

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val menuItem: TextView = itemView.findViewById(R.id.menuItemName)
        val deleteButton: ImageButton = itemView.findViewById(R.id.delete)
        val availabilityButton: ImageButton = itemView.findViewById(R.id.stock)
        val editCardView: MaterialCardView = itemView.findViewById(R.id.menuItemCardView)
    }
}