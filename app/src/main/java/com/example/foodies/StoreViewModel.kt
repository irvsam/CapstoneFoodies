package com.example.foodies

import androidx.lifecycle.ViewModel
import classes.Store

class StoreViewModel : ViewModel() {
    val storeList = mutableListOf<Store>()
    val imageList = mutableListOf(R.drawable.cc,R.drawable.afriquezeen)
}