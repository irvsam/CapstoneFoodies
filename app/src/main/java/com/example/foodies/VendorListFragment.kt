package com.example.foodies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import classes.Adapter
import classes.DietaryReq
import classes.Menu
import classes.Review
import classes.STORE_EXTRA
import classes.Store
import classes.StoreClickListener
import classes.storeList
import java.sql.Time


class VendorListFragment : Fragment() {


    private var campusCafeMenu : Menu = Menu()
    private var afriquezeenMenu: Menu = Menu()
    private var ccReviewList : ArrayList<Review> = ArrayList()
    private var imageList : List<Int> = listOf(R.drawable.cc,R.drawable.afriquezeen)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_vendor_list, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateStores()
        // Assign employeelist to ItemAdapter
        val itemAdapter= Adapter(storeList)
        // Set the LayoutManager that
        // this RecyclerView will use.
        val recyclerView:RecyclerView=view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        // adapter instance is set to the
        // recyclerview to inflate the items.
        recyclerView.adapter = itemAdapter


    }

    private fun populateStores(){
        //campusCafeMenu.addItem(filterCoffee)
        //campusCafeMenu.addItem(icedCoffee)
        val campusCafe: Store = Store("Campus Cafe","Beverages", campusCafeMenu, 4.2, Time(8,15,0),
            Time(16,15,0),
            DietaryReq.VEGETARIAN,ccReviewList,imageList[0])
        val afriquezeen: Store = Store("Afriquezeen","Hearty meals", afriquezeenMenu, 4.8, Time(8,15,0),
            Time(16,15,0),
            DietaryReq.NUT_FREE,ccReviewList,imageList[1])
        storeList.add(campusCafe)
        storeList.add(afriquezeen)
    }



}

