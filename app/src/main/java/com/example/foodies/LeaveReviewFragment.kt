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

class LeaveReviewFragment : Fragment() {
    private lateinit var ratingBar: RatingBar
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
        ratingBar = rootView.findViewById(R.id.ratingBar)
        cleanlinessRatingBar = rootView.findViewById(R.id.cleanlinessRatingBar)
        friendlinessRatingBar = rootView.findViewById(R.id.friendlinessRatingBar)
        efficiencyRatingBar = rootView.findViewById(R.id.efficiencyRatingBar)
        reviewText = rootView.findViewById(R.id.reviewText)
        submitReviewButton = rootView.findViewById(R.id.submitReviewButton)

        // Set a click listener for the Submit Review button
        submitReviewButton.setOnClickListener {
            // Get the ratings and review text from the user
            val overallRating = ratingBar.rating
            val cleanlinessRating = cleanlinessRatingBar.rating
            val friendlinessRating = friendlinessRatingBar.rating
            val efficiencyRating = efficiencyRatingBar.rating
            val userReview = reviewText.text.toString()

            // You can perform actions like sending the review to a server or storing it locally
            // For this example, we'll just display a Toast message
            val reviewMessage = "Overall Rating: $overallRating\n" +
                    "Cleanliness Rating: $cleanlinessRating\n" +
                    "Friendliness Rating: $friendlinessRating\n" +
                    "Efficiency Rating: $efficiencyRating\n" +
                    "User Review: $userReview"

            Toast.makeText(requireContext(), reviewMessage, Toast.LENGTH_LONG).show()
        }

        return rootView
    }
}
