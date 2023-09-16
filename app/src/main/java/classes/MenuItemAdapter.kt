package classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.foodies.ApplicationCore
import com.example.foodies.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MenuItemAdapter(private val menuItemList: List<Entities.MenuItem?>, val lifecycleOwner: LifecycleOwner): RecyclerView.Adapter<MenuItemAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.menu_item_card_cell,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return menuItemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = menuItemList[position]
        if (currentItem != null) {
            holder.menuItem.text = currentItem.name
            holder.deleteButton.setOnClickListener {
                // Use the existing coroutine scope from onBindViewHolder
                lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                        if (currentItem != null) {
                            ApplicationCore.database.menuItemDao().deleteItem(currentItem)
                        }
                }
            }
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val menuItem: TextView = itemView.findViewById(R.id.menuItemName)
        val deleteButton: ImageView = itemView.findViewById(R.id.delete)
    }
}