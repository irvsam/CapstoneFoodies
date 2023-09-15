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
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Date

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
        private val qualityTitle: TextView = itemView.findViewById(R.id.foodQualityTextView)
        private val cleanliness: TextView = itemView.findViewById(R.id.cleanlinessRatingTextView)
        private val cleanlinessTitle: TextView = itemView.findViewById(R.id.cleanlinessTextView)
        private val friendliness: TextView = itemView.findViewById(R.id.friendlinessRatingTextView)
        private val friendlinessTitle: TextView = itemView.findViewById(R.id.friendlinessTextView)
        private val efficiency: TextView = itemView.findViewById(R.id.efficiencyRatingTextView)
        private val efficiencyTitle: TextView = itemView.findViewById(R.id.efficiencyTextView)
        private val comment: TextView = itemView.findViewById(R.id.commentTextView)
        private val timestamp: TextView = itemView.findViewById(R.id.timestampTextView)
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

                val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
                val formattedDate = dateFormat.format(Date(review.timestamp))
                ratingBar.rating = review.overAllRating
                timestamp.text = formattedDate
                comment.text = review.text


                // Check each rating and hide the corresponding TextView if it's 0.0
                if (review.quality.toDouble() == 0.0) {
                    quality.visibility = View.GONE
                    qualityTitle.visibility  = View.GONE

                } else {
                    quality.text = review.quality.toString()
                }

                if (review.cleanliness.toDouble() == 0.0) {
                    cleanliness.visibility = View.GONE
                    cleanlinessTitle.visibility  = View.GONE
                } else {
                    cleanliness.text = review.cleanliness.toString()
                }

                if (review.friendliness.toDouble() == 0.0) {
                    friendliness.visibility = View.GONE
                    friendlinessTitle.visibility  = View.GONE
                } else {
                    friendliness.text = review.friendliness.toString()
                }

                if (review.efficiency.toDouble() == 0.0) {
                    efficiency.visibility = View.GONE
                    efficiencyTitle.visibility  = View.GONE
                } else {
                    efficiency.text = review.efficiency.toString()
                }


            }
        }
    }
}
