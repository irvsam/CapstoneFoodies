package com.example.foodies

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import classes.Entities
import classes.GuestViewModel
import classes.VendorViewModel
import classes.daos.ScanDao
import com.example.foodies.databaseManagement.ApplicationCore
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.StringBuilder

class StoreDetailsFragment : Fragment() {

    private lateinit var guestViewModel: GuestViewModel
    private var storeMenu: List<Entities.MenuItem?>? = null
    private lateinit var imageView: ImageView
    private lateinit var storeName: TextView
    private lateinit var menuTextView: TextView
    private lateinit var reviewTextView: TextView
    private lateinit var reviewButton: Button
    private lateinit var vendorViewModel: VendorViewModel
    private lateinit var numRatings: TextView
    // LiveData to hold the calculated averages
    private val averageScansData = MutableLiveData<List<Float>>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_store_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView = view.findViewById(R.id.imageView)
        storeName = view.findViewById(R.id.storeName)
        menuTextView = view.findViewById(R.id.menu)
        reviewTextView = view.findViewById(R.id.reviewTextView)
        reviewButton = view.findViewById(R.id.reviewButton)
        numRatings = view.findViewById(R.id.numReviewsTextView)
        vendorViewModel = ViewModelProvider(requireActivity())[VendorViewModel::class.java]

        //Remove the review button when displaying a store because Vendors cant review
        if(vendorViewModel.user?.type=="Vendor"){
            reviewButton.visibility = View.GONE
        }

        val store = vendorViewModel.vendor

        CoroutineScope(Dispatchers.IO).launch {
            storeMenu = ApplicationCore.database.vendorDao().getMenuItemsByMenuId(store?.menuId)
            val menu = displayMenuItems(storeMenu).toString()
            withContext(Dispatchers.Main) {
                if (store != null) {
                    imageView.setImageResource(store.image)
                    storeName.text = store.name
                    if (menu.length != 0) {
                        menuTextView.text = menu
                    } else {
                        menuTextView.text = getString(R.string.currently_no_menu_to_display)
                    }
                    val numReviews = ApplicationCore.database.vendorDao().getReviewCountForVendor(store.id)

                    if(numReviews!=0){
                        numRatings.text = "("+numReviews.toString()+")"}
                    else{numRatings.text =""}

                    vendorViewModel.ratingLiveData.observe(viewLifecycleOwner) { rating ->
                        if (rating != null) {
                            reviewTextView.text = rating.toString()

                        }
                        else{
                            reviewTextView.text = "no reviews yet"
                        }
                    }

                    val vendorId = store.id
                    vendorViewModel.loadVendorInitialRating(vendorId)

                    reviewTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                    reviewTextView.setOnClickListener {
                        //navigate to viewing the reviews
                        if(numReviews!=0)
                        {val navController = findNavController()
                        navController.navigate(R.id.viewReviewsFragment)}
                        else{
                            Toast.makeText(requireContext(), "no reviews to show!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }

            val actionBar: ActionBar? = (activity as AppCompatActivity).supportActionBar
            actionBar?.setDisplayHomeAsUpEnabled(true)

            //setting guest behavior
            guestViewModel = ViewModelProvider(requireActivity())[GuestViewModel::class.java]
            if (guestViewModel.isGuest) {
                reviewButton.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey
                    )
                )
            }
            reviewButton.setOnClickListener {
                if (!guestViewModel.isGuest) {
                    // User is logged in, take them to the QR code scanner
                        val navController = findNavController()
                        navController.navigate(R.id.QRFragment)
                } else {
                    //they are guest
                    Toast.makeText(requireContext(), "you are not logged in!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        val barChart: BarChart = view.findViewById(R.id.barChart)

        // get list of averages



        // Sample data (replace with your actual data)
        val sampleData = listOf(
            BarEntry(0f, 3f),   // Hour 0: Average busy-ness of 3
            BarEntry(1f, 5f),   // Hour 1: Average busy-ness of 5
            BarEntry(2f, 2f),   // Hour 2: Average busy-ness of 2
        )
        // Loop 8 times to fill each Bar with the average of all
        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(true)
        barChart.description.isEnabled

        val dataSet = BarDataSet(sampleData, "Average Busy-ness")
        dataSet.setColors(Color.BLUE)
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f

        val barData = BarData(dataSet)

        barChart.data = barData

        barChart.invalidate()

    }

    private fun displayMenuItems(menuItems: List<Entities.MenuItem?>?): StringBuilder {
        val menuItemsString = StringBuilder()

        if (menuItems != null) {
            for (item in menuItems) { // Loop through the items taking their name and price
                val price = String.format("%.2f", item?.price)
                menuItemsString.append("R$price   ")
                menuItemsString.append(item?.name.toString())
                if(item?.inStock==false){
                    menuItemsString.append(" (Out of stock)")
                }
                menuItemsString.append("\n")
            }
        }
        return menuItemsString
    }

    /**
     * Function to return a populated list of the average number of scans per hour for the
     * current Vendor.
     */
    private suspend fun calculateAverageScansForVendor(vendorId: Long) {

        // Retrieve all scans for the current vendor
        val scans = getScansForVendor(vendorId)
        // Create a map in the form: <Int, Scans>
        // to be able to reference all the scans made in a specific hour
        val groupedScans = scans.groupBy { it.hour }
        // create a list that contains 8 "empty" hours
        val averages = MutableList(9) { 0f }

        // Calculate the vendor's average scans for each hour
        for(hour in 9..16) {
            // if there are no scans present for the current hour, fill with an empty list
            val scansForHour = groupedScans[hour] ?: emptyList()
            val average = scansForHour.size.toFloat() / 5
            averages[hour - 9] = average
        }
        // Update the LiveData with the calculated values
        averageScansData.postValue(averages)

    }

    private suspend fun getScansForVendor(vendorId: Long): List<Entities.Scan> {
        var scans: List<Entities.Scan>
        withContext(Dispatchers.IO) {
            scans = ApplicationCore.database.scanDao().getScansByVendor(vendorId)
        }
        return scans
    }


    fun getAverageScansData(vendorId: Long): LiveData<List<Float>> {

        GlobalScope.launch {
            calculateAverageScansForVendor(vendorId)
        }
        return averageScansData
    }

}
