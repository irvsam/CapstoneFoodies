package com.example.foodies
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult


class ScannerActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scanner)

        // Initialise the ZXing scanner
        val integrator = IntentIntegrator(this)
        integrator.setPrompt("Scan a QR Code")
        integrator.setCameraId(0) // Use the rear camera (0 by default)
        integrator.setBeepEnabled(false) // Beep on successful scan

        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Check if the requestCode matches the one used to start the scanner
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            // Check if the scan was successful
            if (resultCode == Activity.RESULT_OK) {
                // Parse the result from the scan
                val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

                // Check if the result is not null
                if (result != null) {
                    // Check if the scanned contents are not null
                    if (result.contents != null) {
                        // The scan was successful, and the result contains the scanned data.
                        // You can now use the "result.contents" to access the scanned QR code data.
                        val scannedResult = result.contents
                        // Handle the scanned result as needed (e.g., display it).
                    } else {
                        // The scan was successful, but the scanned contents are empty.
                        // I think this can happen if the user cancels the scan or if there was an issue with the QR code.
                    }
                } else {
                    // The result is null, which indicates an issue with the scanning process.
                }
            } else {
                // The resultCode indicates that the scan was canceled or there was an issue.
            }
        } else {
            // If the requestCode doesn't match the one used for scanning, pass it to the super class.
            // Idk what this is meant to do I just read about it on a forum
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}