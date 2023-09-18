package com.example.foodies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import classes.Entities
import classes.VendorManagementViewModel
import classes.StoreViewModel
import com.example.foodies.databaseManagement.ApplicationCore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddItemFragment : DialogFragment() {
    private lateinit var storeViewModel: StoreViewModel
    private lateinit var vendorManagementViewModel: VendorManagementViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storeViewModel= ViewModelProvider(requireActivity())[StoreViewModel::class.java]
        vendorManagementViewModel = ViewModelProvider(requireActivity())[VendorManagementViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_add_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemNameInput = view.findViewById<EditText>(R.id.itemNameInput)
        val itemPriceInput = view.findViewById<EditText>(R.id.itemPriceInput)
        val addButton = view.findViewById<Button>(R.id.confirmItemButton)

        addButton.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                val itemName = itemNameInput.text.toString()
                Log.d("Item name", itemName)
                val itemPrice = itemPriceInput.text.toString().toFloat()
                val lastMenuItem = latestItemId()
                val menuItem = Entities.MenuItem(id = lastMenuItem.id+1, menuId = vendorManagementViewModel.vendor!!.menuId, name = itemName, price =itemPrice, inStock = true )
                withContext(Dispatchers.Main){
                    ApplicationCore.database.menuItemDao().insertMenuItem(menuItem)
                    vendorManagementViewModel.addMenuItem(menuItem)
                    dismiss()
                }
            }
        }
    }

    private suspend fun latestItemId():Entities.MenuItem{
        return CoroutineScope(Dispatchers.IO).async{
            val lastID = ApplicationCore.database.menuItemDao().getLastMenuItem()
            lastID
        }.await()
    }

}