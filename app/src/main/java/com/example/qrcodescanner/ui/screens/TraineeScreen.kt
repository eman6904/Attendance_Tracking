//package com.example.qrcodescanner.ui.screens
//
//import androidx.activity.compose.BackHandler
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.People
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.font.Font
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavHostController
//import coil.compose.AsyncImage
//import com.example.qrcodescanner.data.model.AttendanceRegistrationResponse
//import com.example.qrcodescanner.data.utils.getMode
//import com.example.qrcodescanner.ui.components.space
//import com.example.qrcodescanner.R
//import com.example.qrcodescanner.navigation.ScreensRoute
//
//@Composable
//fun TraineeScreen(
//    navController: NavHostController,
//    traineeItems: AttendanceRegistrationResponse
//) {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colors.surface)
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(250.dp)
//                .background(colorResource(id = R.color.mainColor))
//        ) {}
//        screenBody(
//            traineeItems = traineeItems,
//            navController = navController
//        )
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(20.dp),
//            contentAlignment = Alignment.BottomEnd
//        ) {
//            FloatingActionButton(
//                onClick = {
//                    navController.navigate(ScreensRoute.AttendanceScreen.route)
//                },
//                shape = CircleShape,
//                backgroundColor = colorResource(id = R.color.mainColor),
//                modifier = Modifier.size(40.dp)
//            ) {
//                Icon(
//                    imageVector = Icons.Default.People,
//                    tint = Color.White,
//                    contentDescription = null
//                )
//            }
//        }
//    }
//    BackHandler() {
//        navController.navigate(ScreensRoute.ScannerScreen.route)
//    }
//}
//
//@Composable
//fun screenBody(
//    traineeItems: AttendanceRegistrationResponse,
//    navController: NavHostController
//) {
//
//    val buttonColor = remember { mutableStateOf(R.color.cf_color_inL) }
//    if (getMode() == stringResource(id = R.string.light_mode))
//        buttonColor.value = R.color.cf_color_inN
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(30.dp)
//            .clip(RoundedCornerShape(10.dp, 10.dp)),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        space(h = 30)
//        Card(
//            modifier = Modifier
//                .padding(15.dp)
//                .weight(2f),
//            shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
//            elevation = 15.dp
//        ) {
//            if (traineeItems.data?.imageUrl == null) {
//                Icon(
//                    painterResource(R.drawable.profile_photo),
//                    tint = colorResource(id = buttonColor.value),
//                    modifier = Modifier
//                        .fillMaxSize(),
//                    contentDescription = "",
//                )
//            } else {
//                AsyncImage(
//                    modifier = Modifier.fillMaxSize(),
//                    model = traineeItems.data.imageUrl,
//                    contentScale = ContentScale.Crop,
//                    contentDescription = null
//                )
//            }
//        }
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f)
//                .padding(top = 15.dp),
//            contentAlignment = Alignment.TopCenter,
//
//            ) {
//
//            Text(
//                text = traineeItems.data!!.name,
//                color = MaterialTheme.colors.onSurface,
//                textAlign = TextAlign.Center
//            )
//        }
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .weight(1f)
//        ) {
//            Button(
//                onClick = { navController.navigate(ScreensRoute.ScannerScreen.route) },
//                colors = ButtonDefaults.buttonColors(
//                    backgroundColor = colorResource(id = R.color.mainColor)
//                ),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(
//                    text = stringResource(R.string.scan),
//                    color = Color.White,
//                    fontFamily = FontFamily(Font(R.font.bold2)),
//                    modifier = Modifier.padding(3.dp)
//                )
//            }
//            space(h = 15)
//            Button(
//                onClick = { navController.navigate(ScreensRoute.MainScreen.route) },
//                colors = ButtonDefaults.buttonColors(
//                    backgroundColor = colorResource(id = buttonColor.value)
//                ),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(
//                    text = stringResource(R.string.home),
//                    color = Color.White,
//                    fontFamily = FontFamily(Font(R.font.bold2)),
//                    modifier = Modifier.padding(3.dp),
//                )
//            }
//        }
//    }
//}