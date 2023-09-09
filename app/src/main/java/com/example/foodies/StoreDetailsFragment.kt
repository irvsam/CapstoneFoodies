package com.example.foodies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import classes.Entities
import classes.GuestViewModel
import classes.STORE_EXTRA
import classes.STORE_MENU_EXTRA
import classes.Store
import classes.storeList
import com.example.foodies.databinding.FragmentStoreDetailsBinding
import com.google.android.material.snackbar.Snackbar
import java.lang.StringBuilder

class StoreDetailsFragment : Fragment() {

    private lateinit var binding: FragmentStoreDetailsBinding
    private lateinit var guestViewModel: GuestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoreDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val storeID = arguments?.getString(STORE_EXTRA)
        val store = arguments?.getSerializable(STORE_EXTRA) as? Entities.Vendor
        val storeMenu = arguments?.getSerializable(STORE_MENU_EXTRA) as? String
        if (store != null) {
            //TODO Set image to a relevant store image
            binding.imageView.setImageResource(store.image)
            binding.storeName.text = store.name
            binding.menu.text = storeMenu
        }

        val actionBar: ActionBar? = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        guestViewModel = ViewModelProvider(requireActivity())[GuestViewModel::class.java]
        if(guestViewModel.isGuest){
            binding.reviewButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey))
        }
        binding.reviewButton.setOnClickListener {
            if(!guestViewModel.isGuest){
                // User is logged in, handle the review action here
                val navController = findNavController()
                navController.navigate(R.id.leaveReviewFragment)
            }
            else{
                Toast.makeText(requireContext(),"you are not logged in!",Toast.LENGTH_SHORT).show() }
        }

    }

    private fun storeFromName(storeID: String?): Entities.Vendor? {
        for (store in storeList) {
            if (store?.name == storeID) {
                return store
            }
        }
        return null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val navController = findNavController()

                // Manually navigate to the VendorListFragment
                navController.navigate(R.id.vendorListFragment)

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
