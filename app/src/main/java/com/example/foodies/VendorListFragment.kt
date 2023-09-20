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
import classes.SharedViewModel
import classes.StoreClickListener
import classes.StoreViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** list of vendors */
class VendorListFragment : Fragment(), StoreClickListener{

    private lateinit var storeViewModel: SharedViewModel
    private lateinit var vendorViewModel: StoreViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Fragment onCreate() called")
        storeViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        vendorViewModel =  ViewModelProvider(requireActivity())[StoreViewModel::class.java]

        /** if the store list hasnt been populated yet get the stores from the database*/
        while(storeViewModel.storeList.isEmpty()){
        CoroutineScope(Dispatchers.IO).launch {

            storeViewModel.getStores()
        }}



    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_vendor_list, container, false)

    }

    /** set up the adapter*/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemAdapter= Adapter(storeViewModel.storeList, this,this)
        val recyclerView:RecyclerView=view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = itemAdapter
    }



    /** open store page if they click on a store*/
    override fun onClick(store: Entities.Vendor?) {
        vendorViewModel.vendor = store
        val navController = findNavController()
        navController.navigate(R.id.storeDetailsFragment)
    }
}

