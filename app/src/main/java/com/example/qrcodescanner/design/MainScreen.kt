package com.example.qrcodescanner.design


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.qrcodescanner.MainActivity.Companion.SELECTED_CAMPP
import com.example.qrcodescanner.MainActivity.Companion.selectedCamp_sharedPref
import com.example.qrcodescanner.MainActivity.Companion.token
import com.example.qrcodescanner.R
import com.example.qrcodescanner.ScreensRoute
import com.example.qrcodescanner.coding.DataClasses.ItemData
import com.example.qrcodescanner.coding.DataClasses.UserData
import com.example.qrcodescanner.coding.classes.BarcodeScanner
import com.example.qrcodescanner.coding.functions.addTrainee
import com.example.qrcodescanner.coding.functions.getAllCamps
import com.example.qrcodescanner.coding.functions.getCurrentCamp
import com.example.qrcodescanner.coding.functions.getCurrentUser
import com.google.gson.Gson
import kotlinx.coroutines.launch

@Composable
fun mainScreen(navController: NavHostController) {

    token = getCurrentUser()!!.token

    val currentCampName=remember{ mutableStateOf("No Camp")}
    if(getCurrentCamp()!=null)
        currentCampName.value= getCurrentCamp()!!.name

    val showReminder = remember { mutableStateOf(false) }
    if (getCurrentCamp() == null) {

        showReminder.value = true
    }
    campSelectionReminder(showReminder = showReminder)
    Column() {

        val modifier = Modifier
            .weight(1f)
            .fillMaxSize()
            .background(colorResource(id = R.color.mainColor))
        headerSection(currentUser = getCurrentUser()!!, modifier = modifier,currentCampName=currentCampName)
        Card(
            shape = RoundedCornerShape(0.dp, 130.dp, 0.dp, 0.dp),
            modifier = Modifier
                .weight(5f)
                .fillMaxSize()
                .background(colorResource(id = R.color.mainColor))
        ) {
            mainContent(navController = navController, showReminder,currentCampName=currentCampName)
        }
    }

}

@Composable
fun mainContent(navController: NavHostController,
                showReminder: MutableState<Boolean>,
                currentCampName:MutableState<String>
) {

    val context = LocalContext.current
    var barcodeScanner = BarcodeScanner(context)
    val barcodeResults = barcodeScanner.barCodeResults.collectAsState()
    var barcodeValue2 = remember { mutableStateOf("") }
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
            button(btnName = "Select Camp", navController, showReminder,currentCampName=currentCampName)
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
                navController,
                showReminder
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            button(btnName = "View Attendance", navController, showReminder,currentCampName)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            button(btnName = "Extra Points", navController, showReminder,currentCampName)
        }

    }
}

@Composable
fun headerSection(
    currentUser: UserData,
    modifier: Modifier,
    currentCampName:MutableState<String>
) {

    Row(
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(start = 10.dp)
        ) {
            Card(
                modifier = Modifier.padding(3.dp),
                shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),

                ) {
                if (currentUser?.photoUrl == null) {
                    Image(
                        painterResource(R.drawable.profile_photo),
                        modifier = Modifier
                            .size(50.dp)
                            .padding(3.dp)
                            .aspectRatio(1f),
                        contentDescription = "",
                    )
                } else {
                    AsyncImage(
                        modifier = Modifier.size(50.dp),
                        model = currentUser.photoUrl,
                        contentDescription = null
                    )
                }
            }
        }
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .fillMaxSize()
                .weight(4f)

        ) {
            Column {
                Text(
                    text = "${currentUser?.firstName} ${currentUser?.middleName}",
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.bold2)),
                    fontSize = 15.sp
                )
                space(h = 3)
                if(currentCampName.value=="No Camp"){
                    Text(
                        text = currentCampName.value,
                        color = Color.Red,
                        fontFamily = FontFamily(Font(R.font.bold2)),
                        fontSize = 12.sp
                    )
                }else{
                    Text(
                        text = currentCampName.value,
                        color = Color.Green,
                        fontFamily = FontFamily(Font(R.font.bold2)),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun button(
    btnName: String,
    navController: NavHostController,
    showReminder: MutableState<Boolean>,
    currentCampName:MutableState<String>
) {

    var selectCampDialog = remember { mutableStateOf(false) }
    if (selectCampDialog.value) {

        Dialog(
            onDismissRequest = { selectCampDialog.value = false }
        ) {
            Card(
                modifier = Modifier
                    .width(300.dp)
                    .height(450.dp), shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                elevation = 10.dp
            ) {

                Column() {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .background(colorResource(id = R.color.mainColor)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Camps",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.bold2))
                        )
                    }
                    radioButtonforSelectCamp(selectCampDialog,currentCampName)
                }
            }
        }
    }
    Button(
        onClick = {

            if (btnName == "Extra Points") {

                if (getCurrentCamp() == null) {
                    showReminder.value = true
                } else {
                    navController.navigate(ScreensRoute.ExtraPointScreen.route)
                }
            } else if (btnName == "View Attendance") {
                if (getCurrentCamp() == null) {
                    showReminder.value = true
                } else {
                    navController.navigate(ScreensRoute.AttendanceScreen.route)
                }
            } else
                selectCampDialog.value = true


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
    navController: NavHostController,
    showReminder: MutableState<Boolean>
) {
    val shutDown = remember { mutableStateOf(false) }
    val scop = rememberCoroutineScope()

    if (barcodeValue != null) {
        shutDown.value = true
        barcodeValue2.value = barcodeValue
    }

    if (shutDown.value) {
        validation(
            shutDown = shutDown,
            onScanBarcode = barcodeScanner::startScan,
            barcodeValue = barcodeValue,
            barcodeScanner = barcodeScanner,
            barcodeValue2 = barcodeValue2,
            navController = navController,
        )
    }

    Button(
        onClick = {
            if (getCurrentCamp() != null) {
                scop.launch {
                    onScanBarcode()
                }
            } else {
                showReminder.value = true
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
    navController: NavHostController,
) {

    val isSuccess = remember { mutableStateOf(false) }
    val message = remember { mutableStateOf("") }
    val lifecycleOwner = LocalLifecycleOwner.current
    val scop = rememberCoroutineScope()
    val showProgress=remember { mutableStateOf(false) }

    if (barcodeValue != null) {

        barcodeValue2.value = barcodeValue
        if (barcodeValue2.value != "Canceled") {

            LaunchedEffect(Unit) {
                addTrainee(
                    traineeId = barcodeValue2.value,
                    isSuccess = isSuccess,
                    message= message,
                    lifecycleOwner= lifecycleOwner,
                    shutDown= shutDown,
                    showProgress=showProgress
                )
            }
        }
    }
    if(barcodeValue2.value.isNotEmpty()){
        shutDown.value=true
    }
    if (shutDown.value) {
        Dialog(
            onDismissRequest = {
                shutDown.value = false
            }
        ) {

            Card(
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp),
                elevation = 10.dp
            ) {
                progressBar(show = showProgress)
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(10.dp)
                ) {

                    Column(
                        modifier = Modifier
                            .weight(3f)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(3.dp))
                        if (barcodeValue2.value == "Canceled") {
                            Icon(
                                painter = painterResource(R.drawable.warning_icon),
                                tint = colorResource(id = R.color.mainColor),
                                contentDescription = null,
                                modifier = Modifier
                                    .weight(1f)
                                    .size(90.dp)

                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                        } else {
                            if(message.value.isNotEmpty()){
                                if (message.value=="Success") {
                                    validId(navController)
                                } else {
                                    invalidId(message)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                barcodeScanner.cancelScan()
                                shutDown.value = false
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
fun validId(navController:NavHostController){
    Column(
        modifier=Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.trainees),
            tint = colorResource(id = R.color.mainColor),
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .weight(1f)
                .clickable {
                    navController.navigate(ScreensRoute.AttendanceScreen.route)
                }
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Success!",
            modifier = Modifier.weight(1f),
            fontSize = 25.sp,
            fontFamily = FontFamily(Font(R.font.bold2))
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Trainee has been added to attendance",
            modifier = Modifier.weight(1f)
        )
    }
}
@Composable
fun invalidId(message:MutableState<String>){
    Row(
        modifier=Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Text(
            text = "Failed",
            modifier = Modifier.padding(5.dp),
            fontSize = 25.sp,
            fontFamily = FontFamily(Font(R.font.bold2))
        )
        Icon(
            imageVector = Icons.Default.Error,
            tint = Color.Red,
            contentDescription = null,
        )

    }
    Spacer(modifier = Modifier.height(20.dp))
    Text(
        text = message.value,
        modifier = Modifier
    )
}
@Composable
fun radioButtonforSelectCamp(selectCampDialog:MutableState<Boolean>,
currentCampName: MutableState<String>) {


    val camps = remember { mutableStateListOf<ItemData>() }
    val showProgressBar = remember { mutableStateOf(false) }
    val itemsCase = remember { mutableStateOf("") }
    val lifecycleOwner = LocalLifecycleOwner.current
    val gson = Gson()

    LaunchedEffect(Unit) {
        getAllCamps(camps = camps, itemsCase = itemsCase, lifecycleOwner = lifecycleOwner)
    }
    val selectedItem = remember { mutableStateOf("") }
    if (getCurrentCamp() != null)
        selectedItem.value = getCurrentCamp()!!.name

    if (itemsCase.value == "No Camps") {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = itemsCase.value)
        }
    } else {
        showProgressBar.value = camps.size == 0
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
                            val json2 = gson.toJson(camp)
                            selectedItem.value = camp.name
                            selectedCamp_sharedPref.edit().putString(SELECTED_CAMPP, json2).apply()
                            currentCampName.value= getCurrentCamp()!!.name
                            selectCampDialog.value=false
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = colorResource(id = R.color.mainColor),
                            unselectedColor = colorResource(id = R.color.mainColor),
                            disabledColor = Color.DarkGray
                        )
                    )
                    Text(
                        text = camp.name,
                        modifier = Modifier.padding(start = 10.dp),
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
fun campSelectionReminder(
    showReminder: MutableState<Boolean>
) {
    if (showReminder.value) {

        Dialog(
            onDismissRequest = {
                showReminder.value = false
            }) {

            Card(
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp),
                elevation = 10.dp
            ) {

                Column(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 10.dp)
                        .fillMaxSize(),
                ) {
                    Text(
                        text = "Hi ${getCurrentUser()!!.firstName} 👋",
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.bold2)),
                    )
                    space(5)
                    Text(
                        text = "Please don't forget to select the camp"
                    )
                    space(h = 10)
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Button(
                            onClick = {
                                showReminder.value = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(id = R.color.mainColor)
                            )
                        ) {
                            Text(
                                text = "OK",
                                fontSize = 12.sp,
                                color = Color.White,
                                fontFamily = FontFamily(Font(R.font.bold2))
                            )
                        }
                    }
                }
            }
        }
    }
}


