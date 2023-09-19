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
import androidx.navigation.fragment.findNavController
import classes.Entities
import classes.AccountViewModel
import classes.StoreViewModel
import com.example.foodies.databaseManagement.ApplicationCore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LeaveReviewFragment : Fragment() {

    private lateinit var accountViewModel: AccountViewModel
    private lateinit var storeViewModel: StoreViewModel
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
        qualityRatingBar = rootView.findViewById(R.id.stockRatingBar)
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

            val filledRatings = listOf(
                qualityRating,
                cleanlinessRating,
                friendlinessRating,
                efficiencyRating
            )

            // Count how many rating bars were filled
            val numberOfRatings = filledRatings.count { it > 0 }
            if (numberOfRatings > 1) {
                // Calculate the average rating based on the filled ratings
                val total = filledRatings.sum()
                val average = total / numberOfRatings


                val userReview = reviewText.text.toString()
                accountViewModel = ViewModelProvider(requireActivity())[AccountViewModel::class.java]
                storeViewModel = ViewModelProvider(requireActivity())[StoreViewModel::class.java]
                user = accountViewModel.user
                vendor = storeViewModel.vendor
                if (user != null) {
                    val review = Entities.Review(
                        userId = user!!.id,
                        vendorId = vendor!!.id,
                        text = userReview,
                        timestamp = System.currentTimeMillis(),
                        overAllRating = average,
                        quality = qualityRating,
                        cleanliness = cleanlinessRating,
                        friendliness = friendlinessRating,
                        efficiency = efficiencyRating

                    )

                    // Launch a coroutine to insert the review into the database
                    lifecycleScope.launch {
                        insertReviewIntoDatabase(review)
                        Toast.makeText(requireContext(), "review submitted", Toast.LENGTH_SHORT)
                            .show()
                        // Navigate back to the previous fragment

                        withContext(Dispatchers.IO) {
                            accountViewModel.updateUserRewardPoints(user!!.id, 10)
                        }
                        withContext(Dispatchers.IO) {
                            ApplicationCore.database.vendorDao()
                                .updateVendorAverageRating(vendor!!.id)
                            val newRating = ApplicationCore.database.vendorDao()
                                .calculateAverageRating(storeViewModel.vendor!!.id)
                            storeViewModel.updateRating(newRating)
                        }

                        val navController = findNavController()
                        navController.popBackStack()
                        navController.navigate(R.id.storeDetailsFragment)
                    }
                }

            } else {
                Toast.makeText(
                    requireContext(),
                    "Please rate at least two aspects.",
                    Toast.LENGTH_SHORT
                )
                    .show()
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
