package com.example.foodies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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

    /** Initialise view models on creation*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vendorManagementViewModel = ViewModelProvider(requireActivity())[VendorManagementViewModel::class.java]

    }

    /** Inflates EditItemFragment UI */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_edit_item, container, false)
    }

    /** Configures the behaviour of interactive controls*/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editName = view.findViewById<EditText>(R.id.editItemNameInput)
        val editPrice = view.findViewById<EditText>(R.id.editItemPriceInput)
        val confirmButton = view.findViewById<Button>(R.id.confirmEditButton)

        editItem(editName,editPrice,confirmButton)
    }

    /** Sets up the onClickListeners for EditText fields and Buttons*/
    private fun editItem(editName:EditText, editPrice:EditText, confirmButton: Button){
        confirmButton.setOnClickListener{
            val itemName = editName.text.toString()
            var itemPrice = 0.0F
            if(checkPrice(editPrice.text.toString())) {
                itemPrice = editPrice.text.toString().toFloat()
                if (itemName.isNotBlank() && itemPrice != null) {
                    CoroutineScope(Dispatchers.IO).launch {

                        val orginalMenuItem =
                            ApplicationCore.database.menuItemDao().getMenuItemById(menuItemID)
                        val editedMenuItem = Entities.MenuItem(
                            id = menuItemID,
                            menuId = vendorManagementViewModel.vendor!!.menuId,
                            name = itemName,
                            price = itemPrice,
                            inStock = true
                        )
                        withContext(Dispatchers.Main) {
                            ApplicationCore.database.menuItemDao().updateMenuItem(editedMenuItem)
                            vendorManagementViewModel.swapEditMenuItem(
                                orginalMenuItem,
                                editedMenuItem
                            )
                            dismiss()
                        }
                    }
                }
                else{
                    activity?.runOnUiThread {
                        Toast.makeText(context, "Please fill in both fields", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                activity?.runOnUiThread {
                    Toast.makeText(context, "Please enter a valid price", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}