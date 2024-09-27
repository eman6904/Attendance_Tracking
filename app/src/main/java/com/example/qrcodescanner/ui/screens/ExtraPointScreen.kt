//package com.example.qrcodescanner.ui.screens
//
//import android.app.Activity
//import android.view.WindowManager
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.Font
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import com.example.qrcodescanner.R
//import com.example.qrcodescanner.data.model.ItemData
//import com.example.qrcodescanner.data.utils.captlization
//import com.example.qrcodescanner.ui.components.errorDialog
//import com.example.qrcodescanner.ui.components.pointsTextField
//import com.example.qrcodescanner.ui.components.searchAboutTraineeForUpdatePoints
//import com.example.qrcodescanner.ui.components.selectPointAction
//import com.example.qrcodescanner.ui.components.space
//import com.example.qrcodescanner.ui.components.updatePointsButton
//import com.example.qrcodescanner.ui.utils.checkPoints
//
//
//@Composable
//fun ExtraPointsScreen(navController: NavHostController) {
//
//    val context = LocalContext.current
//    val activity = context as? Activity
//    activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
//
//    Box(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(colorResource(id = R.color.mainColor)),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            val pointsAction = stringResource(R.string.points_action)
//            val message = stringResource(R.string.this_field_is_required)
//            var pointType = listOf(stringResource(R.string.plus), stringResource(R.string.minus))
//            var selectedAction by remember { mutableStateOf(pointsAction) }
//            var points = remember { mutableStateOf("") }
//            var points2 = remember { mutableStateOf(0L) }
//            var searchedTrainee = remember { mutableStateOf("") }
//            var traineeId = remember { mutableStateOf("") }
//            val traineeNameRequired = remember { mutableStateOf(false) }
//            val pointsValidation = remember { mutableStateOf(false) }
//            val pointActionRequired = remember { mutableStateOf(false) }
//            val showSending = remember { mutableStateOf(false) }
//            val errorMessage = remember { mutableStateOf("") }
//            val shutDownError = remember { mutableStateOf(false) }
//            val pointsError = remember { mutableStateOf(message) }
//            val context = LocalContext.current
//
//            errorDialog(
//                shutDown = shutDownError,
//                errorMessage = errorMessage
//            )
//            if (points.value != "" && !checkPoints(
//                    isInvalid = pointsValidation,
//                    error = pointsError,
//                    points = points.value,
//                    context = context
//                )
//            )
//                points2.value = points.value.toLong()
//            if (searchedTrainee.value.isNotEmpty())
//                traineeNameRequired.value = false
//            if (selectedAction != stringResource(id = R.string.points_action))
//                pointActionRequired.value = false
//
//            Text(
//                text = stringResource(id = R.string.extra_points),
//                fontSize = 30.sp,
//                fontFamily = FontFamily(Font(R.font.bold2)),
//                color = Color.White,
//                modifier = Modifier.padding(bottom = 10.dp)
//            )
//            space(h = 30)
//            searchAboutTraineeForUpdatePoints(searchedTrainee, traineeId)
//            errorMessage(show = traineeNameRequired.value)
//            space(h = 50)
//            pointsTextField(points = points)
//            errorMessage(show = pointsValidation.value, error = pointsError.value)
//            space(h = 50)
//            selectPointAction(
//                itemList = pointType,
//                selectedItem = selectedAction,
//                onItemSelected = { selectedAction = it }
//            )
//            errorMessage(show = pointActionRequired.value)
//            space(h = 25)
//            updatePointsButton(
//                traineeNameRequired,
//                pointsValidation,
//                pointActionRequired,
//                traineeId,
//                points2,
//                selectedAction,
//                onItemSelected = { selectedAction = it },
//                points,
//                searchedTrainee,
//                showSending,
//                shutDownError,
//                errorMessage
//            )
//            if (showSending.value) {
//                Text(
//                    text = stringResource(R.string.sending),
//                    color = Color.White,
//                    textAlign = TextAlign.Center
//                )
//            }
//        }
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(10.dp),
//            contentAlignment = Alignment.TopStart
//        ) {
//            Icon(
//                imageVector = Icons.Default.ArrowBack,
//                tint = Color.White,
//                contentDescription = null,
//                modifier = Modifier
//                    .clickable {
//                        navController.popBackStack()
//                    }
//                    .padding(5.dp)
//            )
//
//        }
//    }
//}
//
//@Composable
//fun traineeModel(
//    active: MutableState<Boolean>,
//    trainee: ItemData,
//    searchedText: MutableState<String>,
//    traineeId: MutableState<String>
//) {
//
//    Text(
//        text = captlization(trainee.name),
//        modifier = Modifier
//            .clickable {
//                searchedText.value = captlization(trainee.name)
//                traineeId.value = trainee.id
//                active.value = false
//            }
//            .fillMaxWidth()
//            .padding(top = 10.dp, bottom = 10.dp),
//        style = TextStyle(
//            color = MaterialTheme.colors.onSurface
//        )
//    )
//
//    Divider(
//        modifier = Modifier
//            .fillMaxWidth()
//            .heightIn(1.dp)
//    )
//}
//
//@Composable
//fun errorMessage(
//    show: Boolean,
//    error: String = stringResource(id = R.string.this_field_is_required)
//) {
//    if (show) {
//
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(start = 15.dp, end = 15.dp),
//            contentAlignment = Alignment.TopStart
//        ) {
//            Text(
//                text = error,
//                color = colorResource(id = R.color.red)
//            )
//        }
//    }
//}