package com.example.qrcodescanner.Front

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.qrcodescanner.Back.classes.BarcodeScanner
import kotlinx.coroutines.launch
@Composable
fun scannerScreen(){

            val context=LocalContext.current
            var barcodeScanner= BarcodeScanner(context)

           Surface(
               modifier=Modifier.fillMaxSize()
           ) {

               val barcodeResults=barcodeScanner.barCodeResults.collectAsState()
               scanBarcode(
                   onScanBarcode = barcodeScanner::startScan,
                   barcodeValue = barcodeResults.value
               )
           }
}
@Composable
fun scanBarcode(
    onScanBarcode:suspend()->Unit,
    barcodeValue:String?,

    ){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        val scop= rememberCoroutineScope()
        var data= rememberSaveable(){"click here"}
        Button(
            modifier= Modifier
                .fillMaxWidth()
                .padding(30.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                backgroundColor = Color.Blue
            ),
            onClick = {
                scop.launch {
                    onScanBarcode()
                }
            }
        ) {
            Text(
                text = "Scan Barcode"
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        val localUriHandler = LocalUriHandler.current
        if(barcodeValue!=null){
            ClickableText(
                text= AnnotatedString(data),
                style = TextStyle(
                    textDecoration = TextDecoration.Underline,
                    color= Color.Blue
                )
            ) {
                localUriHandler.openUri(barcodeValue!!)
            }
        }
    }
}
