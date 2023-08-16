package com.example.foodies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import classes.STORE_EXTRA
import classes.Store
import classes.storeList
import com.example.foodies.databinding.ActivityMainBinding
import com.example.foodies.databinding.ActivityStoreDetailsBinding

class StoreDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoreDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storeID = intent.getStringExtra(STORE_EXTRA)
        val store = storeFromName(storeID)
        if(store!=null){
            binding.imageView.setImageResource(store.image)
            binding.storeName.text = store.name
            //binding.rating.text = store.rating.toString()
        }
    }

    private fun storeFromName(storeID: String?): Store? {
        for(store in storeList){
            if (store.name == storeID){
                return store
            }
        }
        return null
    }
}