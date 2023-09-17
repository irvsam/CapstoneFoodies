package com.example.foodies

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import classes.adapters.ReviewAdapter
import classes.ReviewViewModel
import classes.StoreViewModel
import classes.VendorManagementViewModel
import com.example.foodies.databaseManagement.ApplicationCore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewReviewsFragment : Fragment() {

    private lateinit var reviewsRecyclerView: RecyclerView
    private lateinit var storeViewModel: StoreViewModel
    private lateinit var vendorManagementViewModel: VendorManagementViewModel
    private lateinit var reviewViewModel: ReviewViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storeViewModel =  ViewModelProvider(requireActivity())[StoreViewModel::class.java]
        reviewViewModel =  ViewModelProvider(requireActivity())[ReviewViewModel::class.java]
        vendorManagementViewModel = ViewModelProvider(requireActivity())[VendorManagementViewModel::class.java]
        Log.d(TAG, "view reviews oncreate called")

        val vendorId:Long
        if(vendorManagementViewModel.isVendor && reviewViewModel.fromManagementPage){
            Log.d(TAG, "view reviews oncreate called for vendor")
            vendorId = vendorManagementViewModel.vendor!!.id
        }
        else {vendorId = storeViewModel.vendor!!.id
            Log.d(TAG, "view reviews oncreate called for user")}

        //get all reviews from database and populate the review list
        populateReviews(vendorId)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_reviews, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Sort the reviews list by timestamp in descending order (latest first)
        reviewViewModel.reviewList.sortByDescending { it?.timestamp }
        val itemAdapter= ReviewAdapter(reviewViewModel.reviewList, this)
        val recyclerView:RecyclerView=view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = itemAdapter
    }




    private fun populateReviews(id: Long){
        Log.d(TAG, "view reviews populating")
            CoroutineScope(Dispatchers.IO).launch {
                val allReviews = ApplicationCore.database.reviewDao().getReviewsByVendorId(id)
                withContext(Dispatchers.Main) {
                    //clear it each time
                        reviewViewModel.reviewList.clear()
                        if (allReviews.isNotEmpty()) {
                            for (review in allReviews) {
                               reviewViewModel.reviewList.add(review)
                            }
                        }

                }

            }

    }



}
