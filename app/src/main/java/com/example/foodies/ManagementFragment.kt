package com.example.foodies

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import classes.Entities
import classes.ReviewViewModel
import classes.adapters.MenuItemAdapter
import classes.VendorManagementViewModel
import classes.StoreViewModel
import com.example.foodies.databaseManagement.ApplicationCore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** This is the vendor management page where a vendor can manage their store menu and ratings etc*/
class ManagementFragment: Fragment() {
    private lateinit var vendorManagementViewModel : VendorManagementViewModel
    private lateinit var storeViewModel : StoreViewModel
    private lateinit var reviewViewModel: ReviewViewModel

    /** Initialises various view models*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vendorManagementViewModel = ViewModelProvider(requireActivity())[VendorManagementViewModel::class.java]
        storeViewModel = ViewModelProvider(requireActivity())[StoreViewModel::class.java]
        reviewViewModel = ViewModelProvider(requireActivity())[ReviewViewModel::class.java]
    }

    /** Sets up ManagementFragment UI*/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_management, container, false)
        return view
    }

    /**
     * Configures the behaviour of the various interactive fields
     * */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Load store details
        val storeName: TextView = view.findViewById(R.id.storeNameTextView)
        val reviewTextView:TextView = view.findViewById(R.id.viewReviewsTextView)
        val numRatings: TextView = view.findViewById(R.id.numReviewsTextView2)

        vendorManagementViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                // still loading so do nothing
            } else {
                val vendorUser = vendorManagementViewModel.vendor
                if (vendorUser != null) {
                    storeName.text = vendorUser.name
                }
                //Review data
                    CoroutineScope(Dispatchers.IO).launch {
                        val numReviews = ApplicationCore.database.vendorDao().getReviewCountForVendor(vendorUser!!.id)
                        withContext(Dispatchers.Main) {

                            vendorManagementViewModel.ratingLiveData.observe(viewLifecycleOwner) { rating ->

                                if (rating != null) {
                                    reviewTextView.text = rating.toString()

                                } else {
                                    reviewTextView.text = "no reviews yet"
                                }
                            }
                            if (numReviews != 0) {
                                numRatings.text = "(" + numReviews.toString() + ")"
                            } else {
                                numRatings.text = ""
                            }

                            vendorManagementViewModel.loadVendorInitialRating(vendorUser.id)
                            //clicking on rating to view the reviews
                            reviewTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                            reviewTextView.setOnClickListener {

                                if (numReviews != 0) {
                                    reviewViewModel.fromVendorList = false
                                    reviewViewModel.fromManagementPage = true
                                    val navController = findNavController()
                                    navController.navigate(R.id.viewReviewsFragment)
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "no reviews to show!",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()

                                } } } } } }


        //Set up the adapter
        val recyclerView: RecyclerView = view.findViewById(R.id.menuItemsRecyclerView)
        val itemAdapter = MenuItemAdapter(requireFragmentManager(),vendorManagementViewModel.menuItems.value,this)
        vendorManagementViewModel.menuItems.observe(viewLifecycleOwner){ newMenuItems ->
            newMenuItems?.let {
                itemAdapter.menuItemList = it
                itemAdapter.notifyDataSetChanged()
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = itemAdapter

        // Add an item
        val addItemButton = view.findViewById<ImageButton>(R.id.addItemButton)
        addItemButton.setOnClickListener {
            val addItemFragment = AddItemFragment()
            addItemFragment.show(requireFragmentManager(), "addItemDialog")
        }
    }
}
