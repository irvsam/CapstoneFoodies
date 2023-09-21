package com.example.foodies

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import classes.Entities
import classes.VendorManagementViewModel
import com.example.foodies.databaseManagement.ApplicationCore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConfirmDeleteDialogFragment(private val item: Entities.MenuItem,private val lifecycleOwner: LifecycleOwner):DialogFragment() {

    private lateinit var vendorManagementViewModel: VendorManagementViewModel

    /** Called on dialog creation. Initialises view models*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vendorManagementViewModel = ViewModelProvider(requireActivity())[VendorManagementViewModel::class.java]
    }

    /** Called when dialog UI is created. Inflates the ConfirmDeleteDialog layout*/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dailog_confirm_delete, container, false)
    }

    /** Indicates what the confirm and cancel button must do, when interacted with*/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val confirmDeleteButton = view.findViewById<Button>(R.id.confirmDeleteButton)
        val cancelDeleteButton = view.findViewById<Button>(R.id.cancelDeleteButton)

        confirmDeleteButton.setOnClickListener{
            lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                ApplicationCore.database.menuItemDao().deleteItem(item)
                vendorManagementViewModel.deleteItem(item)
                activity?.runOnUiThread { // This is to ensure that toast appears on
                    Toast.makeText(context, "Item was deleted", Toast.LENGTH_SHORT).show()
                }
                dismiss()
            }
        }
        cancelDeleteButton.setOnClickListener{
            dismiss()
        }

    }

}