package com.example.foodies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import classes.Entities
import classes.UserViewModel
import classes.VendorViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LeaveReviewFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var vendorViewModel: VendorViewModel
    private var vendor : Entities.Vendor? = null
    private var user: Entities.User? = null // Declare the User property as nullable

    private lateinit var qualityRatingBar:RatingBar
    private lateinit var cleanlinessRatingBar: RatingBar
    private lateinit var friendlinessRatingBar: RatingBar
    private lateinit var efficiencyRatingBar: RatingBar
    private lateinit var reviewText: EditText
    private lateinit var submitReviewButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_leave_review, container, false)

        // Initialize views
        qualityRatingBar = rootView.findViewById(R.id.QualityRatingBar)
        cleanlinessRatingBar = rootView.findViewById(R.id.cleanlinessRatingBar)
        friendlinessRatingBar = rootView.findViewById(R.id.friendlinessRatingBar)
        efficiencyRatingBar = rootView.findViewById(R.id.efficiencyRatingBar)
        reviewText = rootView.findViewById(R.id.reviewText)
        submitReviewButton = rootView.findViewById(R.id.submitReviewButton)

        // Set a click listener for the Submit Review button
        submitReviewButton.setOnClickListener {
            // Get the ratings and review text from the user
            val qualityRating = qualityRatingBar.rating
            val cleanlinessRating = cleanlinessRatingBar.rating
            val friendlinessRating = friendlinessRatingBar.rating
            val efficiencyRating = efficiencyRatingBar.rating
            val total = qualityRating+cleanlinessRating+friendlinessRating+efficiencyRating
            val average = total/4

            val userReview = reviewText.text.toString()
            userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
            vendorViewModel = ViewModelProvider(requireActivity())[VendorViewModel::class.java]
            user = userViewModel.user
            vendor = vendorViewModel.vendor
            if(user!=null) {
                val review = Entities.Review(
                    userId = user!!.id,
                    vendorId = vendor!!.id,
                    text = userReview,
                    overAllRating = average,
                    quality = qualityRating,
                    cleanliness = cleanlinessRating,
                    friendliness = friendlinessRating,
                    efficiency = efficiencyRating

                )

                // Launch a coroutine to insert the review into the database
                lifecycleScope.launch {
                    insertReviewIntoDatabase(review)
                    Toast.makeText(requireContext(),"review submitted",Toast.LENGTH_SHORT).show()

                }
            }

        }

        return rootView
    }

    private suspend fun insertReviewIntoDatabase(review: Entities.Review) {
        withContext(Dispatchers.IO) {
            ApplicationCore.database.reviewDao().insertReview(review)
        }
    }
}
