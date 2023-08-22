package com.example.foodies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import classes.STORE_EXTRA
import classes.Store
import classes.storeList
import com.example.foodies.databinding.FragmentStoreDetailsBinding

class StoreDetailsFragment : Fragment() {

    private lateinit var binding: FragmentStoreDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoreDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val storeID = arguments?.getString(STORE_EXTRA)
        val store = storeFromName(storeID)
        if (store != null) {
            binding.imageView.setImageResource(store.image)
            binding.storeName.text = store.name
            binding.menu.text = store.menu.toString()
        }
        val actionBar: ActionBar? = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun storeFromName(storeID: String?): Store? {
        for (store in storeList) {
            if (store.name == storeID) {
                return store
            }
        }
        return null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Handle the back navigation
                val navController = findNavController()
                if (navController.previousBackStackEntry != null) {
                    // If there's a previous fragment in the back stack, pop back to it
                    navController.popBackStack()
                } else {
                    // If not, navigate to the vendor list fragment (or any desired destination)
                    navController.navigate(R.id.action_storeDetailsFragment_to_vendorListFragment)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
