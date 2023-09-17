package com.example.foodies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

// BrowseFragment.kt
class BrowseFragment : Fragment() {

    private lateinit var searchEditText: EditText
    private lateinit var filterOption1: CheckBox
    private lateinit var searchButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_browse, container, false)
        searchEditText = view.findViewById(R.id.searchEditText)
        filterOption1 = view.findViewById(R.id.filterOption1)
        searchButton = view.findViewById(R.id.searchButton)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchButton.setOnClickListener {
            //perform the search
            //check which filters are selected?

        }
    }
}
