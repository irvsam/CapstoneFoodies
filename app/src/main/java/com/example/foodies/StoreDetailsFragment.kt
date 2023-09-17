package com.example.foodies

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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import classes.Entities
import classes.GuestViewModel
import classes.StoreViewModel
import classes.VendorManagementViewModel
import com.example.foodies.databaseManagement.ApplicationCore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    private lateinit var storeViewModel: StoreViewModel
    private lateinit var vendorManagementViewModel: VendorManagementViewModel
    private lateinit var numRatings: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_store_details, container, false)
    }

    //TODO we might want to use shared view model for stores instead of bundling it, ive already set it up for leaving reviews
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView = view.findViewById(R.id.imageView)
        storeName = view.findViewById(R.id.storeName)
        menuTextView = view.findViewById(R.id.menu)
        reviewTextView = view.findViewById(R.id.reviewTextView)
        reviewButton = view.findViewById(R.id.reviewButton)
        numRatings = view.findViewById(R.id.numReviewsTextView)
        storeViewModel = ViewModelProvider(requireActivity())[StoreViewModel::class.java]
        vendorManagementViewModel = ViewModelProvider(requireActivity())[VendorManagementViewModel::class.java]


        //Remove the review button when displaying a store because Vendors cant review
        if(vendorManagementViewModel.user?.type=="Vendor"){
            reviewButton.visibility = View.GONE
        }

        val store = storeViewModel.vendor

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

                    storeViewModel.ratingLiveData.observe(viewLifecycleOwner) { rating ->
                        if (rating != null) {
                            reviewTextView.text = rating.toString()

                        }
                        else{
                            reviewTextView.text = "no reviews yet"
                        }
                    }

                    val vendorId = store.id
                    if (vendorId != null) {
                        storeViewModel.loadVendorInitialRating(vendorId)
                    }

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
}
