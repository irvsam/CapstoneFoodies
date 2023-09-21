package com.example.foodies

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import classes.Entities
import classes.SearchSharedViewModel
import classes.SharedViewModel
import classes.StoreClickListener
import classes.StoreViewModel
import classes.VendorManagementViewModel
import classes.adapters.MenuItemAdapter
import classes.adapters.SearchAdapter

/** The browse page where a user can search the list of vendors*/
class BrowseFragment : Fragment(),StoreClickListener {

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: ImageButton
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var searchSharedViewModel: SearchSharedViewModel
    private lateinit var vendorViewModel: StoreViewModel

    /** Called when BrowseFragment is first created and initialises the various view models*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel=ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        searchSharedViewModel = ViewModelProvider(requireActivity())[SearchSharedViewModel::class.java]
        vendorViewModel = ViewModelProvider(requireActivity())[StoreViewModel::class.java]
    }

    /** Called when fragments UI is first created to inflate the Browse fragment*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_browse, container, false)
        searchEditText = view.findViewById(R.id.searchEditText)
        searchButton = view.findViewById(R.id.searchButton)
        return view
    }

    /** Called when the BrowseFragment is displayed and ready to be interacted with. It initialises the
     * RecyclerView and the SearchAdapter
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.resultRecyclerView)
        val itemAdapter = SearchAdapter(sharedViewModel.storeList,this,this)
        search(recyclerView,itemAdapter)
    }

    /** Returns search results that correspond to the current typed input*/
    private fun search(recyclerView: RecyclerView,itemAdapter: SearchAdapter){
        //Watch the text as the user types
        searchEditText.addTextChangedListener(object : TextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filteredSuggestions = filterSuggestions(s)
                searchSharedViewModel.searchStoreList.observe(viewLifecycleOwner){ newStoreList ->
                    newStoreList?.let {
                        itemAdapter.storeList = filteredSuggestions
                        itemAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No changes required
            }

            override fun afterTextChanged(s: Editable?) {
                // No changes required
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = itemAdapter
    }

    /** Only returns stores that match the current search query*/
    private fun filterSuggestions(word: CharSequence?):MutableList<Entities.Vendor?>{
        val storeList = sharedViewModel.storeList
        val filteredList = mutableListOf<Entities.Vendor?>()

        for (store in storeList!!){
            if (isCharSequenceInString(word!!,store!!.name)){
                filteredList.add(store)
                searchSharedViewModel.addStoreToSearch(store)
            }
        }
        return filteredList
    }

    /** Compares the current query to the starting char sequence of all avaialable store.
     * Returns a Boolean indicating whether its a match or not
     */
    private fun isCharSequenceInString(charSequence: CharSequence, inputString: String): Boolean {
        val charSequenceLower = charSequence.toString().lowercase()
        val inputStringLower = inputString.lowercase()

        return inputStringLower.startsWith(charSequenceLower)
    }

    /** Navigate to store details page when a store is clicked */
    override fun onClick(store: Entities.Vendor?) {
        vendorViewModel.vendor = store
        val navController = findNavController()
        navController.navigate(R.id.storeDetailsFragment)
    }
}
