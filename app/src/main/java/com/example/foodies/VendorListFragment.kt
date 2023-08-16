package com.example.foodies

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class VendorListFragment : Fragment() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var db: SQLiteDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vendor_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // create the databases
        val dbHelper = DatabaseHelper(requireContext())
        val db = dbHelper.writableDatabase // or readableDatabase

        val backToHomeButton = view.findViewById<Button>(R.id.backToHome_button)
        backToHomeButton.setOnClickListener{
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        val accButton = view.findViewById<Button>(R.id.account_button)
        accButton.setOnClickListener{
            val intent = Intent(requireContext(), AccountActivity::class.java)
            startActivity(intent)
        }
    }
}
