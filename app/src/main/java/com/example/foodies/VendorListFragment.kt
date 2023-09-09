package com.example.foodies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.withCreated
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import classes.Adapter
import classes.AppDatabase
import classes.DietaryReq
import classes.Entities
import classes.Menu
import classes.Review
import classes.STORE_EXTRA
import classes.SharedViewModel
import classes.Store
import classes.StoreClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Time


class VendorListFragment : Fragment(), StoreClickListener{

    private lateinit var storeViewModel: SharedViewModel
    private lateinit var tempStore: Store
    private var campusCafeMenu : Menu = Menu()
    private var afriquezeenMenu: Menu = Menu()
    private var ccReviewList : ArrayList<Review> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storeViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        if (storeViewModel.storeList.isEmpty()) {
            populateStores()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vendor_list, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Assign employeelist to ItemAdapter
        val itemAdapter= Adapter(storeViewModel.storeList, this)
        // Set the LayoutManager that this RecyclerView will use.
        val recyclerView:RecyclerView=view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        // adapter instance is set to the recyclerview to inflate the items.
        recyclerView.adapter = itemAdapter


    }

    private fun populateStores() {
        /*val campusCafe: Store = Store("Campus Cafe", "Beverages", campusCafeMenu, 4.2, Time(8, 15, 0),
            Time(16, 15, 0),
            DietaryReq.VEGETARIAN, ccReviewList, R.drawable.coffees)
        val afriquezeen: Store = Store("Afriquezeen", "Hearty meals", afriquezeenMenu, 4.8, Time(8, 15, 0),
            Time(16, 15, 0),
            DietaryReq.NUT_FREE, ccReviewList, R.drawable.curry)

        // Use the ViewModel's storeList to add stores
        storeViewModel.storeList.add(campusCafe)
        storeViewModel.storeList.add(afriquezeen)*/
        CoroutineScope(Dispatchers.IO).launch {
            val allStores = ApplicationCore.database.vendorDao().getAllVendors()
            withContext(Dispatchers.Main){
                if(allStores.isNotEmpty()){
                    for (store in allStores){
                        storeViewModel.storeList.add(store)
                    }
                }
            }
        }

    }

    override fun onClick(store: Entities.Vendor?) {
        // Open a new fragment when a store is clicked
        val storeDetailsFragment = StoreDetailsFragment()
        // Pass the clicked store's information to the new fragment using Bundle
        val bundle = Bundle()
        bundle.putSerializable(STORE_EXTRA, store)
        storeDetailsFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_fragment, storeDetailsFragment)
            .addToBackStack("StoreDetailsFragmentTransaction")
            .commit()
    }

    //this doesnt seem to be working
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Load and populate the data here
        populateStores()
    }
}

