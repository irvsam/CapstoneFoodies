package com.example.foodies

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

/** viewing a list of reviews*/
class ViewReviewsFragment : Fragment() {

    private lateinit var storeViewModel: StoreViewModel
    private lateinit var vendorManagementViewModel: VendorManagementViewModel
    private lateinit var reviewViewModel: ReviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storeViewModel =  ViewModelProvider(requireActivity())[StoreViewModel::class.java]
        reviewViewModel =  ViewModelProvider(requireActivity())[ReviewViewModel::class.java]
        vendorManagementViewModel = ViewModelProvider(requireActivity())[VendorManagementViewModel::class.java]

        val vendorId:Long
        //it is a vendor viewing the reviews for their store*/
        if(vendorManagementViewModel.isVendor && reviewViewModel.fromManagementPage){
            vendorId = vendorManagementViewModel.vendor!!.id
        }
        else {
            //viewing reviews for the store clicked on from the vendor list */
            vendorId = storeViewModel.vendor!!.id

        }
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

        //set up the adapter */
        reviewViewModel.reviewList.value!!.sortByDescending { it?.timestamp }
        val itemAdapter= ReviewAdapter(reviewViewModel.reviewList.value!!, this, reviewViewModel.fromManagementPage)
        val recyclerView:RecyclerView=view.findViewById(R.id.recycler_view)
        reviewViewModel.reviewList.observe(viewLifecycleOwner){ newReviewReply ->
            newReviewReply?.let {
                itemAdapter.reviews = it
                Log.d("OBSERVED","Observed a change in review list")
                itemAdapter.notifyDataSetChanged()
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = itemAdapter
    }




    /** get the reviews from the database and populate */
    private fun populateReviews(id: Long){
        //to clear the list first */
        reviewViewModel.clearReviews()
            CoroutineScope(Dispatchers.IO).launch {
                val allReviews = ApplicationCore.database.reviewDao().getReviewsByVendorId(id)
                withContext(Dispatchers.Main) {

                        if (allReviews.isNotEmpty()) {
                            for (review in allReviews) {
                               reviewViewModel.addReview(review)
                            }
                        }
                }
            }
    }



}
