package com.example.foodies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import classes.MenuItemAdapter
import classes.MenuItemViewModel
import classes.UserViewModel
import classes.VendorViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ManagementFragment: Fragment() {
    private lateinit var menuItemViewModel : MenuItemViewModel
    private lateinit var userViewModel : UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Entry","Entered the mangementFrag")
        menuItemViewModel = ViewModelProvider(requireActivity())[MenuItemViewModel::class.java]
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        loadMenuItems()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_management, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemAdapter = MenuItemAdapter(menuItemViewModel.menuItems)
        val recyclerView:RecyclerView = view.findViewById(R.id.menuItemsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = itemAdapter
    }

    private fun loadMenuItems(){
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("Entered","loadMenuItems")
            val menuItems = ApplicationCore.database.menuItemDao().getAllMenuItems()
            Log.d("VendorID",userViewModel.user?.id.toString())
            Log.d("Passed",menuItems.size.toString())
            withContext(Dispatchers.Main){
                if(menuItemViewModel.menuItems.isEmpty()){
                    if (menuItems.isNotEmpty()){
                        for(item in menuItems){
                            Log.d("Item",item!!.name)
                            menuItemViewModel.menuItems.add(item)
                        }
                    }
                }
            }
        }
    }
}

