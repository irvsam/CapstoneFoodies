package com.example.foodies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import classes.UserViewModel

class RewardsFragment: Fragment()  {

    private lateinit var userViewModel: UserViewModel // Replace with your ViewModel
    private lateinit var rewardAmtTextView: TextView // Reference to the earned points TextView
    private lateinit var vendorSpinner: Spinner // Reference to the vendor selection Spinner


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rewards, container, false)
        rewardAmtTextView = view.findViewById(R.id.rewardAmt)
        // Load the user's points and update the UI
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        // Observe the LiveData for reward points
        userViewModel.userRewardPoints.observe(viewLifecycleOwner) { rewardPoints ->
            rewardAmtTextView.text = rewardPoints.toString()
        }

        // Load the user's initial reward points from the database
        val userId = userViewModel.user?.id // Replace with the actual user ID
        if (userId != null) {
            userViewModel.loadUserInitialRewardPoints(userId)
        }

        return view
    }
}