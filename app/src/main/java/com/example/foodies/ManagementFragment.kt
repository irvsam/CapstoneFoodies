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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import classes.Entities
import classes.adapters.MenuItemAdapter
import classes.VendorManagementViewModel
import classes.StoreViewModel
import com.example.foodies.databaseManagement.ApplicationCore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ManagementFragment: Fragment() {
    private lateinit var vendorManagementViewModel : VendorManagementViewModel
    private lateinit var storeViewModel : StoreViewModel
    private var vendor: Entities.Vendor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Entry","Entered the managementFrag")
        vendorManagementViewModel = ViewModelProvider(requireActivity())[VendorManagementViewModel::class.java]
        storeViewModel = ViewModelProvider(requireActivity())[StoreViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_management, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ENTERED", "Created")

        //loading store details
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

                            reviewTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                            reviewTextView.setOnClickListener {
                                //navigate to viewing the reviews
                                if (numReviews != 0) {
                                    val navController = findNavController()
                                    navController.navigate(R.id.viewReviewsFragment)
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "no reviews to show!",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()

                                }
                            }
                        }
                    }

            }

        }

        //adapter
        val itemAdapter = MenuItemAdapter(vendorManagementViewModel.menuItems, this)
        val recyclerView: RecyclerView = view.findViewById(R.id.menuItemsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = itemAdapter

        //add item button
        val addItemButton = view.findViewById<ImageButton>(R.id.addItemButton)
        addItemButton.setOnClickListener {
            val addItemFragment = AddItemFragment()
            addItemFragment.show(requireFragmentManager(), "addItemDialog")
        }
    }
}
