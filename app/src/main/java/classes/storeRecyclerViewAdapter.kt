package classes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodies.databinding.StoreCardCellBinding

class storeRecyclerViewAdapter(private val stores: List<Store>) : RecyclerView.Adapter<storeRecyclerViewAdapter.MyViewHolder>(){

    class MyViewHolder(private val cardCellBinding: StoreCardCellBinding)
        : RecyclerView.ViewHolder(cardCellBinding.root)
    {
        fun bindStore(store:Store){
            cardCellBinding.imageView.setImageResource(store.image)
            cardCellBinding.storeName.text = store.name
            cardCellBinding.rating.text = store.rating.toString()
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): storeRecyclerViewAdapter.MyViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = StoreCardCellBinding.inflate(from,parent,false)
        return MyViewHolder(binding)

    }

    override fun onBindViewHolder(holder: storeRecyclerViewAdapter.MyViewHolder, position: Int) {
        holder.bindStore(stores[position])
    }

    override fun getItemCount(): Int = stores.size

}