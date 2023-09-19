package classes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import classes.Entities
import classes.StoreClickListener
import classes.StoreViewModel
import com.example.foodies.R
import com.example.foodies.databaseManagement.ApplicationCore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchAdapter(var storeList: MutableList<Entities.Vendor?>, private val clickListener: StoreClickListener, private val lifecycleOwner: LifecycleOwner): RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val storeCardView = LayoutInflater.from(parent.context).inflate(R.layout.store_card_cell, parent, false)
        return MyViewHolder(storeCardView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentStore = storeList[position]
        if(currentStore!=null) {
            holder.name.text = currentStore.name
            holder.image.setImageResource(currentStore.image)
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

            holder.itemView.setOnClickListener {
                clickListener.onClick(currentStore)
            }
        }
    }

    override fun getItemCount(): Int {
        return storeList.size
    }

    class MyViewHolder(storeView: View):RecyclerView.ViewHolder(storeView){
        val name: TextView = itemView.findViewById(R.id.storeName)
        val rating: TextView = itemView.findViewById(R.id.rating)
        var image: ImageView =itemView.findViewById(R.id.imageView)
        var numRatings: TextView =itemView.findViewById(R.id.numReviewsTextView)
    }

}