package com.example.foodies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
            //binding.menu.text = store.menu.toString()
            //binding.rating.text = store.rating.toString()
        }
    }

    private fun storeFromName(storeID: String?): Store? {
        for (store in storeList) {
            if (store.name == storeID) {
                return store
            }
        }
        return null
    }
}
