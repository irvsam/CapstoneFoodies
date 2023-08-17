package classes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodies.databinding.StoreCardCellBinding

class storeRecyclerViewAdapter(
    private val stores: List<Store>,
    private val clickListener: StoreClickListener
) : RecyclerView.Adapter<storeRecyclerViewAdapter.MyViewHolder>(){

    class MyViewHolder(
        private val storeCardCellBinding: StoreCardCellBinding,
        private val clickListener: StoreClickListener
    )
        : RecyclerView.ViewHolder(storeCardCellBinding.root)
    {
        fun bindStore(store:Store){
            storeCardCellBinding.imageView.setImageResource(store.image)
            storeCardCellBinding.storeName.text = store.name
            storeCardCellBinding.rating.text = store.rating.toString()

            storeCardCellBinding.storeCardView.setOnClickListener{
                clickListener.onClick(store)
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): storeRecyclerViewAdapter.MyViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = StoreCardCellBinding.inflate(from,parent,false)
        return MyViewHolder(binding,clickListener)

    }

    override fun onBindViewHolder(holder: storeRecyclerViewAdapter.MyViewHolder, position: Int) {
        holder.bindStore(stores[position])
    }

    override fun getItemCount(): Int = stores.size

}