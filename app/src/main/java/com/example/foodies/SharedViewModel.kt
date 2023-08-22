package com.example.foodies

import androidx.lifecycle.ViewModel
import classes.Store

class SharedViewModel : ViewModel() {
    val storeList = mutableListOf<Store>()
    //var guest = false
    //var loggedIn = false
}