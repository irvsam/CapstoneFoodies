package com.example.foodies

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import classes.Entities
import classes.daos.ScanDao
import com.example.foodies.databaseManagement.ApplicationCore
import classes.StoreViewModel
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar


class QRFragment : Fragment() {

    private lateinit var storeViewModel: StoreViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_qr, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ZXing scanner
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setPrompt("Scan the store's QR Code")
        integrator.setCameraId(0)  // Use the rear camera (0 by default)
        integrator.setBeepEnabled(false)  // Beep on successful scan
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                // The scan was successful
                // scannedResult should be the name of a vendor
                val scannedResult = result.contents

                storeViewModel = ViewModelProvider(requireActivity())[StoreViewModel::class.java]
                val vendorName = storeViewModel.vendor?.name
                if(scannedResult==vendorName)
                {
                // Display success signal
                showToast("$scannedResult code scanned successfully")
                val navController = findNavController()
                navController.navigate(R.id.leaveReviewFragment)
                    // Create scan object
                    val vendorId = storeViewModel.vendor!!.id
                    val currentTime = Calendar.getInstance()
                    val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
                    val scan = Entities.Scan(vendorId = vendorId, hour = currentHour)
                    lifecycleScope.launch {
                        insertScanInBackground(scan)
                    }
                }



                } else {
                    showToast("Incorrect code. Please scan the code for $vendorName")
                    val navController = findNavController()
                    navController.navigate(R.id.storeDetailsFragment)
                }

            } else {
                // The scan was successful, but the scanned contents are empty.
                // This can happen if the user cancels the scan or if there was an issue with the QR code.
            }
        } else {
            // The result is null, which indicates an issue with the scanning process.
            // IDK what to do here
        }


    private suspend fun insertScanInBackground(scan: Entities.Scan) {
        withContext(Dispatchers.IO) {
            ApplicationCore.database.scanDao().insert(scan)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

