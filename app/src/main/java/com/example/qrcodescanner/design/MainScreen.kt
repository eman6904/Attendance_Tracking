package com.example.qrcodescanner.design

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.qrcodescanner.R
import com.example.qrcodescanner.ScreensRoute
import com.example.qrcodescanner.coding.classes.BarcodeScanner
import kotlinx.coroutines.launch

@Composable
fun mainScreen(navHostController: NavHostController) {


    val context = LocalContext.current
    var barcodeScanner = BarcodeScanner(context)
    val barcodeResults = barcodeScanner.barCodeResults.collectAsState()
    Column() {

        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(colorResource(id = R.color.mainColor))
        ) {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp)
            ) {
//                Image(
//                    painterResource(R.drawable.blue_logo),
//                    modifier = Modifier
//                        .size(60.dp)
//                        // .padding(top = 10.dp, bottom = 5.dp, start = 5.dp, end = 5.dp)
//                        .aspectRatio(1f)
//                        .clip(CircleShape),
//                    contentDescription = "",
//                )
            }
//            Box(
//                contentAlignment = Alignment.CenterStart,
//                modifier = Modifier.fillMaxSize().padding(5.dp)
//            ){
//                Text(text="ICPC sohag Trainings",
//                    color = Color.White,
//                    fontSize = 20.sp
//                )
//            }
        }
        Card(
            shape = RoundedCornerShape(0.dp, 130.dp, 0.dp, 0.dp),
            modifier = Modifier
                .weight(5f)
                .fillMaxSize()
                .background(colorResource(id = R.color.mainColor))
        ) {

            Column(
                modifier = Modifier
                    .padding(top = 50.dp, bottom = 50.dp, start = 35.dp, end = 35.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    button(btnName = "Select Camp",navHostController)
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    scanQrCodeButton(
                        onScanBarcode = barcodeScanner::startScan,
                        barcodeValue = barcodeResults.value,
                        barcodeScanner
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    button(btnName = "View Attendance",navHostController)
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    button(btnName = "Extra Points",navHostController)
                }

            }
        }
    }

}

@Composable
fun button(btnName: String,navController:NavHostController) {
    Button(
        onClick = {

                  if(btnName=="Extra Points")
                      navController.navigate(ScreensRoute.ExtraPointScreen.route)
            else if(btnName=="View Attendance")
                  navController.navigate(ScreensRoute.AttendanceScreen.route)

        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp, 0.dp, 30.dp, 0.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.mainColor),
            contentColor = Color.White
        )
    ) {
        Text(
            text = btnName, modifier = Modifier.padding(10.dp),
            fontFamily = FontFamily(Font(R.font.bold2)),
            fontSize = 17.sp
        )
    }
}

@Composable
fun scanQrCodeButton(
    onScanBarcode: suspend () -> Unit,
    barcodeValue: String?,
    barcodeScanner: BarcodeScanner
) {
    val shutDown = remember { mutableStateOf(false) }
    val scop = rememberCoroutineScope()
    if (shutDown.value && barcodeValue != null) {
        validation(
            shutDown = shutDown,
            onScanBarcode = barcodeScanner::startScan,
            barcodeValue = barcodeValue ,
            barcodeScanner= barcodeScanner
        )
    }

    Button(
        onClick = {
            scop.launch {
                onScanBarcode()
                shutDown.value = true
            }
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp, 0.dp, 30.dp, 0.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.mainColor),
            contentColor = Color.White
        )
    ) {
        Text(
            text = "Scan QrCode", modifier = Modifier.padding(10.dp),
            fontFamily = FontFamily(Font(R.font.bold2)),
            fontSize = 17.sp
        )
    }
}

@Composable
fun validation(
    shutDown: MutableState<Boolean>,
    barcodeValue: String?,
    onScanBarcode: suspend () -> Unit,
    barcodeScanner: BarcodeScanner
) {

    if (shutDown.value) {

        Dialog(
            onDismissRequest = { shutDown.value = false }
        ) {

            Card(
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp),
                elevation = 10.dp
            ) {
                Column(verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(15.dp)
                ) {
                    val scop = rememberCoroutineScope()
                        Column(
                            modifier = Modifier
                                .weight(3f)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (barcodeValue != "Canceled") {
                                Image(
                                    painterResource(R.drawable.done_icon),
                                    modifier = Modifier
                                        .size(90.dp)
                                        .aspectRatio(1f),
                                    contentDescription = "",
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = barcodeValue!!,
                                modifier = Modifier
                            )
                        }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxSize()
                            .weight(1f)
                           ,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                             barcodeScanner.cancelScan()
                             shutDown.value=false
                            },
                            modifier = Modifier.width(IntrinsicSize.Max),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Gray,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Cancel", modifier = Modifier,
                                fontFamily = FontFamily(Font(R.font.bold2)),
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(5.dp))
                        Button(
                            onClick = {
                                scop.launch {
                                    onScanBarcode()
                                    shutDown.value = true
                                }
                            },
                            modifier = Modifier.width(IntrinsicSize.Max),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(id = R.color.mainColor),
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Scan QrCode", modifier = Modifier,
                                fontFamily = FontFamily(Font(R.font.bold2)),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}




