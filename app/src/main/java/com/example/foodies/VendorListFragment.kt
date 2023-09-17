package com.example.foodies

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import classes.adapters.Adapter
import classes.Entities
import classes.StoreListViewModel
import classes.StoreClickListener
import classes.VendorViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class VendorListFragment : Fragment(), StoreClickListener{

    private lateinit var storeViewModel: StoreListViewModel
    private lateinit var vendorViewModel: VendorViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Fragment onCreate() called")
        storeViewModel = ViewModelProvider(requireActivity())[StoreListViewModel::class.java]
        vendorViewModel =  ViewModelProvider(requireActivity())[VendorViewModel::class.java]


        //launch a thread to get the store list from the database
        //make sure that the list is fully updated before moving on
        while(storeViewModel.storeList.isEmpty()){
        CoroutineScope(Dispatchers.IO).launch {
            //get the stores from the database
            storeViewModel.getStores()
        }}



    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vendor_list, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //set up the adapter to display the stores
        super.onViewCreated(view, savedInstanceState)
        // Assign store list to ItemAdapter
        val itemAdapter= Adapter(storeViewModel.storeList, this,this)
        // Set the LayoutManager that this RecyclerView will use.
        val recyclerView:RecyclerView=view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        // adapter instance is set to the recyclerview to inflate the items.
        recyclerView.adapter = itemAdapter
    }




    override fun onClick(store: Entities.Vendor?) {
        vendorViewModel.vendor = store
        // Open a new fragment when a store is clicked
        val navController = findNavController()
        navController.navigate(R.id.storeDetailsFragment)
    }
}

