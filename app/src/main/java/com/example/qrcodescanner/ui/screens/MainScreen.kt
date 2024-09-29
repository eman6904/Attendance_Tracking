package com.example.qrcodescanner.ui.screens


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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.qrcodescanner.MainActivity.Companion.token
import com.example.qrcodescanner.R
import com.example.qrcodescanner.data.model.UserData
import com.example.qrcodescanner.data.utils.*
import com.example.qrcodescanner.ui.components.campReminder
import com.example.qrcodescanner.ui.components.errorDialog
import com.example.qrcodescanner.ui.components.mainButtons
import com.example.qrcodescanner.ui.components.menuItems
import com.example.qrcodescanner.ui.components.scanQrCodeButton
import com.example.qrcodescanner.ui.components.space


@Composable
fun MainScreen(navController: NavHostController) {

    token = getCurrentUser()!!.token

    val context = LocalContext.current
    val curCamp = stringResource(R.string.no_camp)
    val currentCampName = remember { mutableStateOf(curCamp) }
    if (getCurrentCamp() != null)
        currentCampName.value = getCurrentCamp()!!.name

    val showReminder = remember { mutableStateOf(false) }
    if (getCurrentCamp() == null) {

        showReminder.value = true
    }
    campReminder(showReminder = showReminder)

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
fun headerSection(
    currentUser: UserData,
    modifier: Modifier,
    currentCampName: MutableState<String>
) {
    Row(
        modifier = modifier.padding(start = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .size(50.dp),
            shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),

            ) {
            if (currentUser?.photoUrl == null) {
                Image(
                    painterResource(R.drawable.profile_photo),
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Inside,
                    contentDescription = "",
                )
            } else {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = currentUser.photoUrl,
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }
        }
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp,end=35.dp)
        ) {
            Column {
                Text(
                    text = "${currentUser?.firstName} ${currentUser?.middleName}",
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.bold2)),
                    fontSize = 15.sp
                )
                space(h = 3)
                if (currentCampName.value == stringResource(id = R.string.no_camp)) {
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
            mainButtons(
                btnName = stringResource(R.string.select_camp),
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
            mainButtons(
                btnName = stringResource(R.string.view_attendance),
                navController,
                showReminder,
                currentCampName
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            mainButtons(
                btnName = stringResource(R.string.extra_points),
                navController,
                showReminder,
                currentCampName
            )
        }

    }
}
