package com.example.foodies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    // LiveData to trigger the "View Vendors" action
    val onViewVendorsAction: MutableLiveData<Unit> = MutableLiveData()
    private val onBackToHomeAction: MutableLiveData<Unit> = MutableLiveData()

    // Method to trigger the "View Vendors" action
    fun onViewVendorsClick() {
        // Unit is essentially the same as 'void' in Java, I'm pretty sure
        onViewVendorsAction.value = Unit
    }

    fun onBackToHomeClick(){
        onBackToHomeAction.value = Unit
    }
}
