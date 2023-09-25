package classes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.foodies.R
import classes.Entities
import com.example.foodies.ReplyReviewFragment
import com.example.foodies.databaseManagement.ApplicationCore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date

/** this class acts as the adapter for the list of reviews*/
class ReviewAdapter(var reviews: MutableList<Entities.Review?>,
                    private val lifecycleOwner: LifecycleOwner,
                    private val isVendorFromManagement: Boolean) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_reviews, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /** set it to show reviews starting with the latest review at the top */
        val reversePosition = itemCount - 1 - position
        val review = reviews[reversePosition]
        holder.bind(review)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /** all the UI items */
        private val userNameTextView: TextView = itemView.findViewById(R.id.userName)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        private val quality: TextView = itemView.findViewById(R.id.stockRatingTextView)
        private val qualityTitle: TextView = itemView.findViewById(R.id.stockTextView)
        private val cleanliness: TextView = itemView.findViewById(R.id.cleanlinessRatingTextView)
        private val cleanlinessTitle: TextView = itemView.findViewById(R.id.cleanlinessTextView)
        private val friendliness: TextView = itemView.findViewById(R.id.friendlinessRatingTextView)
        private val friendlinessTitle: TextView = itemView.findViewById(R.id.friendlinessTextView)
        private val efficiency: TextView = itemView.findViewById(R.id.efficiencyRatingTextView)
        private val efficiencyTitle: TextView = itemView.findViewById(R.id.efficiencyTextView)
        private val comment: TextView = itemView.findViewById(R.id.commentTextView)
        private val timestamp: TextView = itemView.findViewById(R.id.timestampTextView)
        private val avatar: ImageView = itemView.findViewById(R.id.avatarImageView)
        private val reply: TextView = itemView.findViewById(R.id.replyTextView)
        private val replyHeading: TextView = itemView.findViewById(R.id.replyHeading)


        /** method to bind UI elements with the review data*/
        fun bind(review: Entities.Review?) {
            if(review!=null){
                userNameTextView.text = "Loading..." /** this is just if it takes time to retrieve */

                /** fetch the username on a thread*/
                lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    val userName = ApplicationCore.database.accountDao().getUsernameById(review.userId)

                    withContext(Dispatchers.Main) {
                        userNameTextView.text = userName
                    }
                }/** fetch the avatar on a thread*/
                lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    val user = ApplicationCore.database.accountDao().getUserById(review.userId)

                    withContext(Dispatchers.Main) {
                        if (user != null) {
                            setAvatarImage(user.avatar, avatar)
                        }
                    }
                }
                /** timestamp format */
                val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
                val formattedDate = dateFormat.format(Date(review.timestamp))
                ratingBar.rating = review.overAllRating
                timestamp.text = formattedDate
                comment.text = review.text

                if(comment.text.isEmpty()){comment.visibility = View.GONE}

                /** these are to decide which rating criteria to show, if the user didn't set a rating then remove it from
                 * the view */
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

                /** set the reply logic if it is a vendor viewing reviews from their management page */
                if(isVendorFromManagement){
                    if(review.reply == null){
                        reply.text = "Reply"
                        replyHeading.visibility = View.GONE
                        reply.setOnClickListener {view->
                            val replyFragment = ReplyReviewFragment(review)
                            val context = view.context
                            val fragmentManager = (context as AppCompatActivity).supportFragmentManager
                            replyFragment.show(fragmentManager,"ReplyReviewDialog")
                        }
                    }
                    else{reply.text = review.reply}
                }

                else{
                    /** set reply logic if it is just someone there to view */
                    if (review.reply ==null){
                        reply.visibility = View.GONE
                        replyHeading.visibility = View.GONE
                    }
                    else{
                        reply.text = review.reply

                    }
                }
            }
        }
    }

    /** setting the avatar image*/
    private fun setAvatarImage(avatarName: String?, avatarImageView: ImageView?) {
        avatarName?.let {
            val resourceId = avatarMap[it]
            if (resourceId != null) {
                avatarImageView?.setImageResource(resourceId)
            }
        }
    }

    /** avatar map */
    private val avatarMap = mapOf(
        "penguin" to R.drawable.penguin,
        "rabbit" to R.drawable.rabbit,
        "sloth" to R.drawable.sloth,
        "camel" to R.drawable.camel,
        "secret" to R.drawable.secret
    )
}
