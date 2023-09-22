package com.example.foodies

import android.content.ContentValues.TAG
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
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
import classes.ReviewViewModel
import classes.StoreViewModel
import classes.VendorManagementViewModel
import com.example.foodies.databaseManagement.ApplicationCore
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalTime

/** Storefront page displaying the relevant details regarding the store*/
class StoreDetailsFragment : Fragment() {

    private lateinit var guestViewModel: GuestViewModel
    private var storeMenu: List<Entities.MenuItem?>? = null
    private lateinit var imageView: ImageView
    private lateinit var storeName: TextView
    private lateinit var menuTextView: TextView
    private lateinit var reviewTextView: TextView
    private lateinit var reviewButton: Button
    private lateinit var description: TextView
    private lateinit var storeViewModel: StoreViewModel
    private lateinit var vendorManagementViewModel: VendorManagementViewModel
    private lateinit var reviewViewModel: ReviewViewModel
    private lateinit var numRatings: TextView
    private lateinit var openClosed: TextView
    private lateinit var hours: TextView
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
        openClosed = view.findViewById(R.id.OpenClosedTextView)
        hours = view.findViewById(R.id.HoursTextView)
        menuTextView = view.findViewById(R.id.menu)
        reviewTextView = view.findViewById(R.id.reviewTextView)
        reviewButton = view.findViewById(R.id.reviewButton)
        numRatings = view.findViewById(R.id.numReviewsTextView)
        description = view.findViewById(R.id.storeDescriptionTextView)
        storeViewModel = ViewModelProvider(requireActivity())[StoreViewModel::class.java]
        vendorManagementViewModel = ViewModelProvider(requireActivity())[VendorManagementViewModel::class.java]
        reviewViewModel = ViewModelProvider(requireActivity())[ReviewViewModel::class.java]



        //Vendor can't leave a review
        if(vendorManagementViewModel.user?.type=="Vendor"){
            reviewButton.visibility = View.GONE
        }
        //Store whos page we are on
        val store = storeViewModel.vendor

        //Set up the menu and other details
        CoroutineScope(Dispatchers.IO).launch {
            storeMenu = ApplicationCore.database.vendorDao().getMenuItemsByMenuId(store?.menuId)
            val menu = displayMenuItems(storeMenu).toString()
            withContext(Dispatchers.Main) {
                if (store != null) {
                    imageView.setImageResource(store.image)
                    storeViewModel.loadDescription(store.id)
                    storeName.text = store.name
                    if (menu.length != 0) {
                        menuTextView.text = menu
                    } else {
                        menuTextView.text = getString(R.string.currently_no_menu_to_display)
                    }
                    val numReviews =
                        ApplicationCore.database.vendorDao().getReviewCountForVendor(store.id)

                    if (numReviews != 0) {
                        numRatings.text = "(" + numReviews.toString() + ")"
                    } else {
                        numRatings.text = ""
                    }

                    storeViewModel.ratingLiveData.observe(viewLifecycleOwner) { rating ->
                        if (rating != null) {
                            reviewTextView.text = rating.toString()

                        } else {
                            reviewTextView.text = "no reviews yet"
                        }
                    }

                    val vendorId = store.id
                    storeViewModel.loadVendorInitialRating(vendorId)

                    //Click on rating to go to review list
                    reviewTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                    reviewTextView.setOnClickListener {
                        if(numReviews!=0)

                        {   reviewViewModel.fromManagementPage = false
                            reviewViewModel.fromVendorList = true

                            val navController = findNavController()
                        navController.navigate(R.id.viewReviewsFragment)}
                        else{
                            Toast.makeText(requireContext(), "no reviews to show!", Toast.LENGTH_SHORT)
                                .show()

                        }
                    }
                }

                val d = storeViewModel.vendor?.description
                description.text = d
                val storeHours = "${store?.openTime} - ${store?.closeTime}"
                hours.text = storeHours
                val openTime = LocalTime.parse(store?.openTime)
                val closeTime = LocalTime.parse(store?.closeTime)
                val currentTime = LocalTime.now()
                val isOpen = currentTime.isAfter(openTime) && currentTime.isBefore(closeTime)

                if(isOpen){
                    openClosed.text = "Open"
                    val colorResource = R.color.nicegreen
                    //set the text colour to green
                    openClosed.setTextColor(ContextCompat.getColor(requireContext(), colorResource))
                }



            }

            val actionBar: ActionBar? = (activity as AppCompatActivity).supportActionBar
            actionBar?.setDisplayHomeAsUpEnabled(true)

            //Guest user cannot leave reviews, update UI
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
                    //Logged on user can go to the QR scanner
                    val navController = findNavController()
                    navController.navigate(R.id.QRFragment)
                } else {
                    //Guest cannot
                    Toast.makeText(requireContext(), "you are not logged in!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        observeAverageScansData(store!!.id)
    }
    private fun calculateAverageScansForVendor(vendorId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            val scanDataForVendor = getScansForVendor(vendorId)
            // Group scan data by hour and initialize an array to store averages
            val groupedData = scanDataForVendor.groupBy { it.hour }
            val averages = MutableList(9) { 0f } // Initialize with 0 for each hour

            // Calculate average scans for each hour
            for (hour in 9..16) { // Hours from 9 am to 5 pm
                val scansForHour = groupedData[hour] ?: emptyList()
                val average = if (scansForHour.isNotEmpty()) {
                    (scansForHour.size / 7.0).toFloat() // Average without rounding
                } else {
                    0f // No scans for this hour
                }
                averages[hour - 9] = average
            }
            withContext(Dispatchers.Main) {
                // Update the LiveData with the calculated averages
                averageScansData.postValue(averages)
            }
        }
    }

    private suspend fun getScansForVendor(vendorId: Long): List<Entities.Scan> {
        var scans: List<Entities.Scan>
        withContext(Dispatchers.IO) {
            scans = ApplicationCore.database.scanDao().getScansByVendor(vendorId)
        }
        return scans
    }

    private fun getAverageScansData(vendorId: Long): LiveData<List<Float>> {
        calculateAverageScansForVendor(vendorId)
        return averageScansData
    }

    /** Creates the bar graph based on the scans read in from the database */
    private fun observeAverageScansData(vendorId: Long) {
        val barChart: BarChart = requireView().findViewById(R.id.barChart)

        getAverageScansData(vendorId).observe(viewLifecycleOwner) { averages ->
            // Update the graph using the 'averages' data
            val data = mutableListOf<BarEntry>()
            for ((index, average) in averages.withIndex()) {
                data.add(BarEntry(index.toFloat(), average))
            }

            // Set up labels for the x-axis
            val xLabels = listOf("9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00")
            val dataSet = BarDataSet(data, "Avg. number of reviews per day")

            // Customize the appearance of the dataset
            dataSet.color = Color.BLUE
            dataSet.valueTextColor = Color.BLACK
            dataSet.valueTextSize = 12f

            // Set the data for the BarChart
            val barData = BarData(dataSet)
            barChart.data = barData

            // Customize the appearance of the x-axis
            val xAxis = barChart.xAxis
            xAxis.valueFormatter = IndexAxisValueFormatter(xLabels)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.setDrawAxisLine(true)
            xAxis.granularity = 1f // Set granularity to 1 to avoid displaying non-integer values

            // Customize the left Y-axis (hide labels)
            val leftAxis = barChart.axisLeft
            leftAxis.setDrawLabels(false)

            // Customize the right Y-axis (hide labels)
            val rightAxis = barChart.axisRight
            rightAxis.setDrawLabels(false)

            // Additional BarChart customization
            barChart.setDrawBarShadow(false)
            barChart.description = null
            barChart.setDrawValueAboveBar(true)
            barChart.legend.isEnabled = false
            dataSet.setDrawValues(false)
            barChart.invalidate()
        }
    }

    /** display the menu items */
    private fun displayMenuItems(menuItems: List<Entities.MenuItem?>?): StringBuilder {
        val menuItemsString = StringBuilder()

        if (menuItems != null) {
            for (item in menuItems) { // Loop through the items taking their name and price
                val price = String.format("%.2f", item?.price)
                menuItemsString.append("R$price   ")
                menuItemsString.append(item?.name.toString())
                if (item?.inStock == false) {
                    menuItemsString.append(" (Out of stock)")
                }
                menuItemsString.append("\n")
            }
        }
        return menuItemsString
    }




}
