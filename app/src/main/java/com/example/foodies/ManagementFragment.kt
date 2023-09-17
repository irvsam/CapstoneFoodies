package com.example.foodies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import classes.Entities
import classes.adapters.MenuItemAdapter
import classes.MenuItemViewModel
import classes.VendorViewModel

class ManagementFragment: Fragment() {
    private lateinit var menuItemViewModel : MenuItemViewModel
    private lateinit var vendorViewModel : VendorViewModel
    private var vendor: Entities.Vendor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Entry","Entered the managementFrag")
        menuItemViewModel = ViewModelProvider(requireActivity())[MenuItemViewModel::class.java]
        vendorViewModel = ViewModelProvider(requireActivity())[VendorViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_management, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ENTERED", "Created")
        val itemAdapter = MenuItemAdapter(menuItemViewModel.menuItems,this)
        val recyclerView:RecyclerView = view.findViewById(R.id.menuItemsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = itemAdapter
        val addItemButton = view.findViewById<ImageButton>(R.id.addItemButton)
        addItemButton.setOnClickListener{
            val addItemFragment = AddItemFragment()
            addItemFragment.show(requireFragmentManager(),"addItemDialog")
        }
    }
}

