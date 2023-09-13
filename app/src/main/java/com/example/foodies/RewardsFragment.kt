package com.example.foodies

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import classes.UserViewModel
import classes.VendorRepository
import classes.VendorViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RewardsFragment: Fragment()  {

    private lateinit var userViewModel: UserViewModel
    private lateinit var autoCompleteUserTextView: AutoCompleteTextView
    private lateinit var adapterItems: ArrayAdapter<String>
    private val vendorRepository: VendorRepository = VendorRepository()




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


        // I want to get the list of vendors and load it into the auto complete text thing

        super.onViewCreated(view, savedInstanceState)

        // Reference to the AutoCompleteTextView
        autoCompleteUserTextView = view.findViewById(R.id.vendor_input)

        // Create an empty mutable list as the initial data source for the ArrayAdapter
        val vendorList = mutableListOf<String>()

        // Create an ArrayAdapter with the initial empty list
        adapterItems = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, vendorList)

        // Set the adapter to the AutoCompleteTextView
        autoCompleteUserTextView.setAdapter(adapterItems)

        // Set a threshold for AutoCompleteTextView (e.g., 1 character)
        autoCompleteUserTextView.threshold = 1

        // Launch a coroutine to fetch the vendor list
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val fetchedVendorList = vendorRepository.getVendorList()

                // Update the mutable list with the fetched vendor list
                vendorList.clear()
                vendorList.addAll(fetchedVendorList)

                // Notify the adapter that the data has changed
                adapterItems.notifyDataSetChanged()
            } catch (e: Exception) {
                // Handle any exceptions that may occur during the data fetch
                Log.e(TAG, "Error fetching vendor list: ${e.message}", e)
            }
        }

        // Handle item selection from the AutoCompleteTextView (as shown in the previous response)
        autoCompleteUserTextView.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position) as String
            // Handle the selected item here
            Toast.makeText(requireContext(), "Selected Vendor: $selectedItem", Toast.LENGTH_SHORT).show()
        }


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