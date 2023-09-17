package com.example.foodies
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import classes.VendorViewModel
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult


class QRFragment : Fragment() {

    private lateinit var vendorViewModel: VendorViewModel

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

                vendorViewModel = ViewModelProvider(requireActivity())[VendorViewModel::class.java]
                val vendorName = vendorViewModel.vendor?.name
                if(scannedResult==vendorName)
                {
                // Display success signal
                showToast("$scannedResult code scanned successfully")
                val navController = findNavController()
                navController.navigate(R.id.leaveReviewFragment)}

                else{
                    showToast("Incorrect code. Please scan the code for $vendorName")
                    val navController = findNavController()
                    navController.navigate(R.id.storeDetailsFragment)}

            } else {
                // The scan was successful, but the scanned contents are empty.
                // This can happen if the user cancels the scan or if there was an issue with the QR code.
            }
        } else {
            // The result is null, which indicates an issue with the scanning process.
            // IDK what to do here
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
