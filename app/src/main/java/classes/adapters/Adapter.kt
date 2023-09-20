package classes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import classes.Entities
import classes.StoreClickListener
import com.example.foodies.databaseManagement.ApplicationCore
import com.example.foodies.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Adapter(private val storeList: MutableList<Entities.Vendor?>, private val clickListener: StoreClickListener, private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<Adapter.MyViewHolder>() {

    // This method creates a new ViewHolder object for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // Inflate the layout for each item and return a new ViewHolder object
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.store_card_cell, parent, false)
        return MyViewHolder(itemView)
    }

    /**
     * Returns the total number of items in the data set
     */
    override fun getItemCount(): Int {
        return storeList.size
    }

    /** This method binds the data to the ViewHolder object
    for each item in the RecyclerView */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentStore = storeList[position]
        assignStoresCardDetails(holder,currentStore)

    }

    /**
     assignStoreCardDetails() is used to assign details(name, rating, store image) to the current store within the RecyclerViews ViewHolder.
     It also allows sets up what must happen in the event that a user clicks on the store card
     */
    private fun assignStoresCardDetails(holder:MyViewHolder,currentStore: Entities.Vendor?){
        if (currentStore!= null) {
            holder.name.text = currentStore?.name

            lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {

                val rating = ApplicationCore.database.vendorDao().calculateAverageRating(currentStore.id)
                val numReviews = ApplicationCore.database.vendorDao().getReviewCountForVendor(currentStore.id)

                // Update the UI on the main thread
                withContext(Dispatchers.Main) {
                    if(numReviews!=0){
                        holder.rating.text = rating.toString()
                        holder.numRatings.text = "("+numReviews.toString()+")"}
                    else{
                        holder.numRatings.text =""
                        holder.rating.text = "no reviews yet"}
                }
            }

            holder.image.setImageResource(currentStore.image)
            // Set a click listener for the store card
            holder.itemView.setOnClickListener {
                clickListener.onClick(currentStore)
            }
        }
    }


    /**
     This class defines the ViewHolder object for each item in the RecyclerView
      */
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.storeName)
        val rating: TextView = itemView.findViewById(R.id.rating)
        var image: ImageView=itemView.findViewById(R.id.imageView)
        var numRatings: TextView=itemView.findViewById(R.id.numReviewsTextView)
    }
}

