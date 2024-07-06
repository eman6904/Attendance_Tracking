package com.example.qrcodescanner.design

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

import androidx.navigation.NavHostController
import com.example.qrcodescanner.MainActivity.Companion.SELECTED_CAMP
import com.example.qrcodescanner.MainActivity.Companion.sharedPreferences
import com.example.qrcodescanner.R
import com.example.qrcodescanner.ScreensRoute

import com.example.qrcodescanner.coding.DataClasses.ItemDetails
import com.example.qrcodescanner.coding.classes.BarcodeScanner
import com.example.qrcodescanner.coding.functions.addTrainee
import com.example.qrcodescanner.coding.functions.getAllCamps
import kotlinx.coroutines.launch

@Composable
fun mainScreen(navHostController: NavHostController) {


    val context = LocalContext.current
    var barcodeScanner = BarcodeScanner(context)
    val barcodeResults = barcodeScanner.barCodeResults.collectAsState()
    var barcodeValue2= remember { mutableStateOf("")}
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
                Card(
                    modifier = Modifier,
                    shape =RoundedCornerShape(20.dp,20.dp,20.dp,20.dp),

                    ){
                    Image(
                        painterResource(R.drawable.icpc_yellow_logo),
                        modifier = Modifier
                            .size(60.dp)
                            .aspectRatio(1f)
                        ,
                        contentDescription = "",
                    )
                }
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
                        barcodeScanner,
                        barcodeValue2 = barcodeValue2,
                        navHostController
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

    var selectCamp= remember { mutableStateOf(false)}
    if(selectCamp.value){

        Dialog(
            onDismissRequest = { selectCamp.value = false }
        ) {
            Card(
                modifier = Modifier
                    .width(300.dp)
                    .height(450.dp)
                ,shape = RoundedCornerShape(10.dp,10.dp,10.dp,10.dp),
                elevation = 10.dp
            ) {

              Column(){
                  Box(
                      modifier = Modifier
                          .fillMaxWidth()
                          .height(80.dp)
                          .background(colorResource(id = R.color.mainColor)),
                      contentAlignment = Alignment.Center
                  ){
                      Text(
                          text="All Camps",
                          color=Color.White,
                          fontSize = 20.sp,
                          fontFamily = FontFamily(Font(R.font.bold2))
                      )
                  }
                  radioButtonforSelectCamp()
              }
            }
        }
    }
    Button(
        onClick = {

            if(btnName=="Extra Points")
                navController.navigate(ScreensRoute.ExtraPointScreen.route)
            else if(btnName=="View Attendance")
                navController.navigate(ScreensRoute.AttendanceScreen.route)
            else
                selectCamp.value=true


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
    barcodeScanner: BarcodeScanner,
    barcodeValue2: MutableState<String>,
    navController: NavHostController
) {
    val shutDown = remember { mutableStateOf(false) }
    val scop = rememberCoroutineScope()

    if(barcodeValue!=null){
        shutDown.value = true
        barcodeValue2.value=barcodeValue
    }

    if (shutDown.value ) {
        validation(
            shutDown = shutDown,
            onScanBarcode = barcodeScanner::startScan,
            barcodeValue = barcodeValue ,
            barcodeScanner= barcodeScanner,
            barcodeValue2 = barcodeValue2,
            navController = navController
        )
    }

    Button(
        onClick = {
            scop.launch {
                onScanBarcode()
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
    barcodeScanner: BarcodeScanner,
    barcodeValue2: MutableState<String>,
    navController: NavHostController
) {

    val isSuccess=remember{ mutableStateOf(false)}
    val message=remember{ mutableStateOf("")}

    if(barcodeValue!=null){
        shutDown.value = true
        barcodeValue2.value=barcodeValue
    }
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
                    modifier = Modifier.padding(10.dp)
                ) {
                    val scop = rememberCoroutineScope()
                    Column(
                        modifier = Modifier
                            .weight(3f)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(3.dp))
                       if(barcodeValue2.value=="Canceled"){
                           Icon(
                               painter = painterResource(R.drawable.warning_icon),
                               tint = colorResource(id = R.color.mainColor),
                               contentDescription = null,
                               modifier = Modifier
                                   .weight(1f)
                                   .size(90.dp)
                                   .clickable {

                                   }
                           )
                           Spacer(modifier = Modifier.height(20.dp))
                           Column(horizontalAlignment = Alignment.CenterHorizontally){
                               Text(
                                   text = "Are You Sure?",
                                   modifier = Modifier,
                                   fontSize = 25.sp,
                                   fontFamily = FontFamily(Font(R.font.bold2))
                               )
                               Spacer(modifier = Modifier.height(10.dp))
                               Text(
                                   text = "You Want To Cancel Scanning",
                                   modifier = Modifier
                               )
                           }
                       }else{
                           addTrainee(traineeId = barcodeValue2.value, isSuccess = isSuccess,message)
                          if(isSuccess.value){

                                  Icon(
                                      painter = painterResource(R.drawable.trainees),
                                      tint = colorResource(id = R.color.mainColor),
                                      contentDescription = null,
                                      modifier = Modifier
                                          .size(80.dp)
                                          .clickable {
                                              navController.navigate(ScreensRoute.AttendanceScreen.route)
                                          }
                                  )
                              Spacer(modifier = Modifier.height(20.dp))
                              Column(
                                  horizontalAlignment = Alignment.CenterHorizontally
                              ){
                                  Text(
                                      text = "Success!",
                                      modifier = Modifier,
                                      fontSize = 25.sp,
                                      fontFamily = FontFamily(Font(R.font.bold2))
                                  )
                                  Spacer(modifier = Modifier.height(10.dp))
                                  Text(
                                      text = "Trainee has been added to attendance",
                                      modifier = Modifier
                                  )
                              }
                          }else{
                              Icon(
                                  painter = painterResource(R.drawable.failed2),
                                  tint = colorResource(id = R.color.mainColor),
                                  contentDescription = null,
                                  modifier = Modifier
                                      .size(70.dp)
                              )
                              Spacer(modifier = Modifier.height(20.dp))
                              Text(
                                  text = "Failed!",
                                  modifier = Modifier,
                                  fontSize = 25.sp,
                                  fontFamily = FontFamily(Font(R.font.bold2))
                              )
                              Spacer(modifier = Modifier.height(20.dp))
                              Text(
                                  text = message.value,
                                  modifier = Modifier
                              )
                          }
                       }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
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

@Composable
fun radioButtonforSelectCamp() {


    val camps = remember { mutableStateListOf<ItemDetails>() }
    val showProgressBar=remember{ mutableStateOf(false)}
    val itemsCase=remember{ mutableStateOf("")}

    getAllCamps(camps = camps, itemsCase = itemsCase)

    val savedCamp =sharedPreferences.getString(SELECTED_CAMP, null)
    val selectedItem = remember { mutableStateOf(savedCamp) }
//    if(camps.size>0)
//    Log.d("iddddddddddddd",camps.find{ it.name==savedCamp }!!.id)
    if(itemsCase.value=="No Camps")
    {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text(text=itemsCase.value)
        }
    }else{
        showProgressBar.value=camps.size==0
        progressBar(show = showProgressBar)
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            camps.forEach() { camp ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth()
                ) {
                    RadioButton(
                        modifier = Modifier.size(15.dp),
                        selected = camp.name == selectedItem.value,
                        onClick = {
                            selectedItem.value = camp.name
                            sharedPreferences.edit().putString(SELECTED_CAMP, selectedItem.value).apply()
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = colorResource(id = R.color.mainColor),
                            unselectedColor = colorResource(id = R.color.mainColor),
                            disabledColor = Color.DarkGray
                        )
                    )
                    Text(text = camp.name, modifier = Modifier.padding(start = 10.dp), fontSize = 20.sp)
                }
            }
        }
    }
}


