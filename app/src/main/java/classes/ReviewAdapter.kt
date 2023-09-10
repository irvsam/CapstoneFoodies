package classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.foodies.R // Replace with the appropriate resource import
import classes.Entities // Import your entity package
import com.example.foodies.ApplicationCore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReviewAdapter(private val reviews: MutableList<Entities.Review?>, private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_reviews, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = reviews[position]
        holder.bind(review)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userNameTextView: TextView = itemView.findViewById(R.id.userName)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        private val quality: TextView = itemView.findViewById(R.id.qualityRatingTextView)
        private val cleanliness: TextView = itemView.findViewById(R.id.cleanlinessRatingTextView)
        private val friendliness: TextView = itemView.findViewById(R.id.friendlinessRatingTextView)
        private val efficiency: TextView = itemView.findViewById(R.id.efficiencyRatingTextView)
        private val comment: TextView = itemView.findViewById(R.id.commentTextView)
        // Bind review data to the UI elements here
        fun bind(review: Entities.Review?) {
            if(review!=null){
                userNameTextView.text = "Loading..." // Show loading message while fetching username

                // Start a coroutine to fetch the username
                lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    val userName = ApplicationCore.database.accountDao().getUsernameById(review.userId)

                    // Update the UI on the main thread
                    withContext(Dispatchers.Main) {
                        userNameTextView.text = userName
                    }
                }

                ratingBar.rating = review.overAllRating
                quality.text= review.quality.toString()
                cleanliness.text = review.cleanliness.toString()
                friendliness.text = review.friendliness.toString()
                efficiency.text = review.efficiency.toString()
                comment.text = review.text

            }
        }
    }
}
