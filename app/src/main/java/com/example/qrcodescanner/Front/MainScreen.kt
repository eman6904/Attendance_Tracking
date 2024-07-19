package com.example.qrcodescanner.Front


import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
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
import com.example.qrcodescanner.Back.DataClasses.AddTraineeRequirements
import com.example.qrcodescanner.Back.DataClasses.ItemData
import com.example.qrcodescanner.Back.DataClasses.UserData
import com.example.qrcodescanner.Back.functions.*
import com.example.qrcodescanner.MainActivity
import com.google.gson.Gson

@Composable
fun mainScreen(navController: NavHostController) {

    token = getCurrentUser()!!.token

    val context = LocalContext.current
    val currentCampName = remember { mutableStateOf("No Camp") }
    if (getCurrentCamp() != null)
        currentCampName.value = getCurrentCamp()!!.name

    val showReminder = remember { mutableStateOf(false) }
    if (getCurrentCamp() == null) {

        showReminder.value = true
    }
    campSelectionReminder(showReminder = showReminder)

    val errorMessage = remember { mutableStateOf("") }
    val shutDownError = remember { mutableStateOf(false) }
    errorDialog(shutDownError, errorMessage)

    val showMenu = remember { mutableStateOf(false) }
    if (showMenu.value)
        menuItems(
            navController = navController,
            showMenu = showMenu,
            errorMessage = errorMessage,
            shutDownError = shutDownError
        )

    Box() {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            val modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(colorResource(id = R.color.mainColor))
            headerSection(
                currentUser = getCurrentUser()!!,
                modifier = modifier,
                currentCampName = currentCampName
            )
            Card(
                shape = RoundedCornerShape(0.dp, 130.dp, 0.dp, 0.dp),
                modifier = Modifier
                    .weight(5f)
                    .fillMaxSize()
                    .background(colorResource(id = R.color.mainColor))
            ) {
                mainContent(
                    navController = navController,
                    showReminder = showReminder,
                    currentCampName = currentCampName,
                )
            }

        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            IconButton(
                onClick = { showMenu.value = !showMenu.value }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "menu icon",
                    tint = Color.White
                )
            }
        }
    }
    BackHandler() {
        // Exit the app when the back button is pressed
        (context as? Activity)?.finish()
    }
}
@Composable
fun mainContent(
    navController: NavHostController,
    showReminder: MutableState<Boolean>,
    currentCampName: MutableState<String>,
) {

    Column(
        modifier = Modifier
            .padding(top = 50.dp, bottom = 50.dp, start = 35.dp, end = 35.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            button(
                btnName = "Select Camp",
                navController,
                showReminder,
                currentCampName = currentCampName
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            scanQrCodeButton(
                showReminder = showReminder,
                navController = navController,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            button(btnName = "View Attendance", navController, showReminder, currentCampName)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            button(btnName = "Extra Points", navController, showReminder, currentCampName)
        }

    }
}
@Composable
fun headerSection(
    currentUser: UserData,
    modifier: Modifier,
    currentCampName: MutableState<String>
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
                if (currentCampName.value == "No Camp") {
                    Text(
                        text = currentCampName.value,
                        color = Color.Red,
                        fontFamily = FontFamily(Font(R.font.bold2)),
                        fontSize = 12.sp
                    )
                } else {
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
    currentCampName: MutableState<String>
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
                    radioButtonforSelectCamp(selectCampDialog, currentCampName)
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
    navController: NavHostController,
    showReminder: MutableState<Boolean>,
) {

    Button(
        onClick = {
            if (getCurrentCamp() != null) {
                navController.navigate(ScreensRoute.ScannerScreen.route)
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
    barcodeValue: MutableState<String>,
    navController: NavHostController,
    shutDownError: MutableState<Boolean>,
    errorMessage: MutableState<String>,
) {

    val isSuccess = remember { mutableStateOf(false) }
    val message = remember { mutableStateOf("") }
    val shutDown = remember { mutableStateOf(false) }

    if (barcodeValue.value.isNotEmpty()) {
        LaunchedEffect(Unit) {
            addTraineeToAttendance(
                traineeRequirements = AddTraineeRequirements(
                    barcodeValue.value,
                    getCurrentCamp()!!.id
                ),
                isSuccess = isSuccess,
                message = message,
                shutDown = shutDown,
                errorMessage = errorMessage,
                shutDownError = shutDownError,
            )
        }
    }
    if (shutDown.value) {
        Dialog(
            onDismissRequest = {
                shutDown.value = false
                message.value = ""
                barcodeValue.value = ""
            }
        ) {

            Card(
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp),
                elevation = 10.dp
            ) {
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
                        if (message.value.isNotEmpty()) {
                            if (isSuccess.value) {
                                validId(navController)
                            } else {
                                invalidId(message)
                            }
                        }

                    }
                }
            }
        }
    }
}
@Composable
fun validId(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxWidth(),
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
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Success!",
            modifier = Modifier.weight(1f),
            fontSize = 23.sp,
            fontFamily = FontFamily(Font(R.font.bold2))
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Trainee has been added to attendance",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(15.dp))
    }
}

@Composable
fun invalidId(message: MutableState<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Failed",
            modifier = Modifier.padding(5.dp),
            fontSize = 23.sp,
            fontFamily = FontFamily(Font(R.font.bold2))
        )
        Icon(
            imageVector = Icons.Default.Error,
            tint = Color.Red,
            contentDescription = null,
        )

    }
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = message.value,
        modifier = Modifier,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(15.dp))
}

@Composable
fun radioButtonforSelectCamp(
    selectCampDialog: MutableState<Boolean>,
    currentCampName: MutableState<String>
) {


    val camps = remember { mutableStateListOf<ItemData>() }
    val showProgressBar = remember { mutableStateOf(false) }
    val itemsCase = remember { mutableStateOf("") }
    val gson = Gson()
    val errorMessage = remember { mutableStateOf("") }
    val shutDownError = remember { mutableStateOf(false) }

    if (camps.isEmpty() && itemsCase.value.isEmpty())
        showProgressBar.value = true
    else
        showProgressBar.value = false

    LaunchedEffect(Unit) {
        getCamps(
            camps = camps,
            itemsCase = itemsCase,
            showProgress = showProgressBar,
            shutDownError = shutDownError,
            errorMessage = errorMessage
        )
    }
    val selectedItem = remember { mutableStateOf("") }
    if (getCurrentCamp() != null)
        selectedItem.value = getCurrentCamp()!!.name

    progressBar(show = showProgressBar)
    errorDialog(shutDownError, errorMessage)

    if (itemsCase.value == "No Camps") {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = itemsCase.value)
        }
    } else {

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
                            currentCampName.value = getCurrentCamp()!!.name
                            selectCampDialog.value = false
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

@Composable
fun menuItems(
    navController: NavHostController,
    showMenu: MutableState<Boolean>,
    errorMessage: MutableState<String>,
    shutDownError: MutableState<Boolean>
) {

    val mode = remember { mutableStateOf("") }
    val context = LocalContext.current
    mode.value = getMode().toString()

    if (showMenu.value) {
        Box() {
            DropdownMenu(
                expanded = showMenu.value,
                onDismissRequest = { showMenu.value = false },
                offset = DpOffset(x = (200).dp, y = (5).dp)
            )
            {
                DropdownMenuItem(
                    onClick = {

                        logout(
                            shutDownError = shutDownError,
                            errorMessage = errorMessage,
                            navController = navController
                        )
                        showMenu.value = false
                    }
                ) {
                    Row() {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = null,
                            tint = colorResource(id = R.color.mainColor)
                        )
                        Text(
                            text = "Logout",
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
                DropdownMenuItem(
                    onClick = {

                        when (mode.value) {
                            "Dark Mode" -> {
                                MainActivity.mode_sharedPref.edit()
                                    .putString(MainActivity.MODE, "Light Mode").apply()
                                refresh(context)
                            }
                            "Light Mode" -> {
                                MainActivity.mode_sharedPref.edit()
                                    .putString(MainActivity.MODE, "Dark Mode").apply()
                                refresh(context)
                            }
                        }
                        showMenu.value = false
                    }
                ) {
                    Row() {
                        when (mode.value) {
                            "Dark Mode" -> {
                                Icon(
                                    imageVector = Icons.Default.DarkMode,
                                    contentDescription = null,
                                )
                            }
                            "Light Mode" -> {
                                Icon(
                                    imageVector = Icons.Default.LightMode,
                                    contentDescription = null,
                                )
                            }
                        }
                        Text(
                            text = mode.value,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

