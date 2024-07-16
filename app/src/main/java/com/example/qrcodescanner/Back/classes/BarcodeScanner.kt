package com.example.qrcodescanner.Back.classes

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.flow.MutableStateFlow

class BarcodeScanner(appContext: Context) {

    val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_ALL_FORMATS
        ).build()

    val scanner = GmsBarcodeScanning.getClient(appContext, options)
    val barCodeResults= MutableStateFlow<String?>(null)
    private var scanTask: Task<Barcode>? = null

   suspend fun startScan(){
       try{
          scanTask=scanner.startScan()
               .addOnSuccessListener { barcode ->
                   // Task completed successfully
                   barCodeResults.value=barcode.displayValue
               }
               .addOnCanceledListener {
                   // Task canceled
                   barCodeResults.value="Canceled"
               }
               .addOnFailureListener { e ->
                   // Task failed with an exception
                   barCodeResults.value="Failed"
               }
       }catch (e:Exception){}
   }
    fun cancelScan() {
        scanTask?.let {
            if (!it.isComplete && !it.isCanceled) {
                barCodeResults.value = "Canceled"
            }
        }
    }
}