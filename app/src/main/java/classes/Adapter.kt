package classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodies.R

class Adapter(private val storeList: MutableList<Entities.Vendor?>, private val clickListener: StoreClickListener) : RecyclerView.Adapter<Adapter.MyViewHolder>() {

    // This method creates a new ViewHolder object for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // Inflate the layout for each item and return a new ViewHolder object
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.store_card_cell, parent, false)
        return MyViewHolder(itemView)
    }

    // This method returns the total
    // number of items in the data set
    override fun getItemCount(): Int {
        return storeList.size
    }

    // This method binds the data to the ViewHolder object
    // for each item in the RecyclerView
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentStore = storeList[position]
        if (currentStore!= null) {
            holder.name.text = currentStore?.name
            holder.rating.text = currentStore?.rating.toString()
            //TODO this needs to be changed to set the actual image (temporary)
            holder.image.setImageResource(currentStore.image)
            // Set a click listener for the store card
            holder.itemView.setOnClickListener {
                clickListener.onClick(currentStore)
            }
        }
    }

    // This class defines the ViewHolder object for each item in the RecyclerView
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.storeName)
        val rating: TextView = itemView.findViewById(R.id.rating)
        var image: ImageView=itemView.findViewById(R.id.imageView)

    }
}
