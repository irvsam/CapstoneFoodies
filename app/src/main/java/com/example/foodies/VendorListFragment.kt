package com.example.foodies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import classes.Adapter
import classes.DietaryReq
import classes.Menu
import classes.Review
import classes.STORE_EXTRA
import classes.Store
import classes.StoreClickListener
import java.sql.Time


class VendorListFragment : Fragment(), StoreClickListener{

    private lateinit var storeViewModel: StoreViewModel
    private var campusCafeMenu : Menu = Menu()
    private var afriquezeenMenu: Menu = Menu()
    private var ccReviewList : ArrayList<Review> = ArrayList()
    private var imageList : List<Int> = listOf(R.drawable.cc,R.drawable.afriquezeen)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storeViewModel = ViewModelProvider(requireActivity())[StoreViewModel::class.java]
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
        val itemAdapter= Adapter(storeViewModel.storeList)
        // Set the LayoutManager that this RecyclerView will use.
        val recyclerView:RecyclerView=view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        // adapter instance is set to the recyclerview to inflate the items.
        recyclerView.adapter = itemAdapter


    }

    private fun populateStores() {
        val campusCafe: Store = Store("Campus Cafe", "Beverages", campusCafeMenu, 4.2, Time(8, 15, 0),
            Time(16, 15, 0),
            DietaryReq.VEGETARIAN, ccReviewList, imageList[0])
        val afriquezeen: Store = Store("Afriquezeen", "Hearty meals", afriquezeenMenu, 4.8, Time(8, 15, 0),
            Time(16, 15, 0),
            DietaryReq.NUT_FREE, ccReviewList, imageList[1])

        // Use the ViewModel's storeList to add stores
        storeViewModel.storeList.add(campusCafe)
        storeViewModel.storeList.add(afriquezeen)
    }

    //TODO make the storedetails thing into a fragment not an activity so this can work

    override fun onClick(store: Store) {
        // Open a new fragment when a store is clicked
        val storeDetailsFragment = StoreDetailsFragment()

        // Pass the clicked store's information to the new fragment using Bundle
        val bundle = Bundle()
        bundle.putSerializable("clicked_store", store)
        storeDetailsFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_fragment, storeDetailsFragment)
            .addToBackStack(null)
            .commit()
    }


}

