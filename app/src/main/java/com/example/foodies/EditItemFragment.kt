package com.example.foodies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import classes.Entities
import classes.VendorManagementViewModel
import com.example.foodies.databaseManagement.ApplicationCore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditItemFragment(private val menuItemID: Long) : DialogFragment() {

    private lateinit var vendorManagementViewModel: VendorManagementViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vendorManagementViewModel = ViewModelProvider(requireActivity())[VendorManagementViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_edit_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editName = view.findViewById<EditText>(R.id.editItemNameInput)
        val editPrice = view.findViewById<EditText>(R.id.editItemPriceInput)
        val confirmButton = view.findViewById<Button>(R.id.confirmEditButton)

        confirmButton.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                val itemName = editName.text.toString()
                val itemPrice = editPrice.text.toString().toFloat()
                val orginalMenuItem = ApplicationCore.database.menuItemDao().getMenuItemById(menuItemID)
                val editedMenuItem = Entities.MenuItem(id=menuItemID, menuId = vendorManagementViewModel.vendor!!.menuId, name = itemName, price = itemPrice,inStock = true)
                withContext(Dispatchers.Main){
                    ApplicationCore.database.menuItemDao().updateMenuItem(editedMenuItem)
                    vendorManagementViewModel.swapEditMenuItem(orginalMenuItem,editedMenuItem)

                }
            }
        }
    }

}