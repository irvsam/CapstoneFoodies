package com.example.foodies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorBoundsInfo
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
import classes.STORE_MENU_EXTRA
import classes.SharedViewModel
import classes.Store
import classes.StoreClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.StringBuilder
import java.sql.Time


class VendorListFragment : Fragment(), StoreClickListener{

    private lateinit var storeViewModel: SharedViewModel

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
}

