package com.example.qrcodescanner.ui.screens.trainee


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.qrcodescanner.MainActivity.Companion.viewModelHelper
import com.example.qrcodescanner.R
import com.example.qrcodescanner.data.apis.UIState
import com.example.qrcodescanner.data.model.ItemData
import com.example.qrcodescanner.data.utils.getMode
import com.example.qrcodescanner.data.viewModel.traineeViewModels.AbsenceTraineesViewModel
import com.example.qrcodescanner.data.viewModel.traineeViewModels.PresentTraineesViewModel
import com.example.qrcodescanner.navigation.ScreensRoute
import com.example.qrcodescanner.ui.components.AttendanceTopBar
import com.example.qrcodescanner.ui.components.TabR0w
import com.example.qrcodescanner.ui.components.progressBar
import com.example.qrcodescanner.ui.components.space
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState



@Composable
fun AttendanceScreen(navController: NavHostController,
                     attendanceViewModel: PresentTraineesViewModel = hiltViewModel(),
                     absenceViewModel: AbsenceTraineesViewModel = hiltViewModel(),
                     ) {

    var selectedCase=remember { mutableStateOf(0) }
    var attendanceNumber=remember { mutableStateOf(0) }
    var absenceNumber=remember { mutableStateOf(0) }
    val showProgress = remember { mutableStateOf(false) }
    val itemsCase = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }
    val shutDownError = remember { mutableStateOf(false) }
    var isRefreshing =remember { mutableStateOf(false) }
    val traineesList = remember { mutableStateListOf<ItemData>() }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing.value)
    val selectedTrainee= viewModelHelper.selectedTrainee.collectAsState()
    val attendanceState by attendanceViewModel.state.collectAsState()
    val absenceState by absenceViewModel.state.collectAsState()

    if(selectedCase.value==0){
        when (attendanceState) {

            is UIState.Loading -> {

                if (isRefreshing.value==false)
                    showProgress.value = true
            }

            is UIState.Error -> {

                shutDownError.value = true
                showProgress.value = false
                errorMessage.value = (attendanceState as UIState.Error).message
            }

            is UIState.Success -> {

                LaunchedEffect(Unit) {

                    traineesList.clear()
                    itemsCase.value = ""

                    val attendanceResponse = (attendanceState as UIState.Success).data

                    if(attendanceResponse.message=="Unauthorized."){
                        navController.navigate(ScreensRoute.LogInScreen.route)
                    }else{
                        if (attendanceResponse.message == "No current session for now.")
                            itemsCase.value = "No current session for now."
                        else {
                            if (attendanceResponse.data!!.presentTrainees.size==0) {

                                itemsCase.value = "No trainee has presented yet"

                            } else {
                                val filteredList = attendanceResponse.data.presentTrainees.filter {
                                    it.name.contains(selectedTrainee.value.trim().replace("\\s+".toRegex(), " "), ignoreCase = true)
                                }
                                traineesList.addAll(filteredList)
                                attendanceNumber.value=traineesList.size
                                absenceNumber.value=attendanceResponse.data.totalCount-traineesList.size

                            }
                        }
                    }
                    showProgress.value = false
                    shutDownError.value = false
                }
            }
            else -> {
                Log.d("state", "else")
            }
        }
    }else{
        when (absenceState) {

            is UIState.Loading -> {

                if (isRefreshing.value==false)
                    showProgress.value = true
            }

            is UIState.Error -> {

                shutDownError.value = true
                showProgress.value = false
                errorMessage.value = (absenceState as UIState.Error).message
            }

            is UIState.Success -> {

                LaunchedEffect(Unit) {

                    traineesList.clear()
                    itemsCase.value = ""

                    val absenceResponse = (absenceState as UIState.Success).data

                    if(absenceResponse.message=="Unauthorized."){
                        navController.navigate(ScreensRoute.LogInScreen.route)
                    }else{
                        if (absenceResponse.message == "No current session for now.")
                            itemsCase.value = "No current session for now."
                        else {
                            if (absenceResponse.data.size==0) {

                                itemsCase.value = "No trainee has presented yet"

                            } else {

                                val filteredList = absenceResponse.data.filter {
                                    it.name.contains(selectedTrainee.value.trim().replace("\\s+".toRegex(), " "), ignoreCase = true)
                                }
                                traineesList.addAll(filteredList)
                            }
                        }
                    }
                    showProgress.value = false
                    shutDownError.value = false
                }
            }
            else -> {
                Log.d("state", "else")
            }
        }
    }
    LaunchedEffect(isRefreshing.value,selectedTrainee.value,selectedCase.value)
    {

        if(isRefreshing.value)
            showProgress.value = false
        else if(traineesList.isEmpty())
            showProgress.value=true

        if(selectedCase.value==0)
        attendanceViewModel.getPresentTrainees(keyWord = "")
        else if(selectedCase.value==1&&attendanceState!=UIState.Loading)
        absenceViewModel.getAbsence()

        kotlinx.coroutines.delay(2000)
        isRefreshing.value= false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AttendanceTopBar(
            navController = navController,
        )
        Column(
            modifier = Modifier
                .padding(top = 5.dp)
                .background(MaterialTheme.colors.surface)

        ) {
            val tabs = listOf("Attendance", "Absence")
            Box(
                modifier=Modifier.fillMaxWidth()
                    .padding(5.dp)
            ){
                TabR0w(
                    selectedCase =selectedCase,
                    tabs=tabs,
                    attendanceNumber = attendanceNumber,
                    absenceNumber = absenceNumber
                    )
            }

            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    isRefreshing.value = true
                },
            ) {
                mainContent(
                    shutDownError = shutDownError,
                    showProgress = showProgress,
                    errorMessage = errorMessage,
                    itemsCase = itemsCase,
                    presentTraineeList = traineesList
                )
            }
        }
    }
}
@Composable
fun mainContent(
    shutDownError:MutableState<Boolean>,
    showProgress:MutableState<Boolean>,
    errorMessage:MutableState<String>,
    itemsCase:MutableState<String>,
    presentTraineeList:MutableList<ItemData>
){
    if(shutDownError.value)
        sadNews(message = errorMessage)
    else{
        if (itemsCase.value == stringResource(R.string.no_trainee_has_presented_yet) ||
            itemsCase.value == stringResource(R.string.no_current_session_for_now)||
            itemsCase.value == stringResource(R.string.not_found)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item{ Text(
                    text = itemsCase.value,
                    color = MaterialTheme.colors.onSurface
                )}
            }
        } else {
            Box(){
                LazyColumn(
                    modifier = Modifier
                        .background(MaterialTheme.colors.surface)
                        .fillMaxSize()
                ) {
                    items(presentTraineeList) { trainee ->

                        traineeModel(
                            traineeName = trainee.name,
                            traineeHandle = trainee.codeForceHandle
                        )
                    }
                }
                progressBar(show = showProgress)
            }
        }
    }

}

@Composable
fun traineeModel(traineeName: String, traineeHandle: String) {

    val cf_color = remember { mutableStateOf(R.color.cf_color_inL) }
    if (getMode() == stringResource(id = R.string.light_mode))
        cf_color.value = R.color.cf_color_inN
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp, bottom = 5.dp)
            .height(90.dp),
        elevation = 15.dp
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,

            ) {

            Icon(
                painter = painterResource(R.drawable.person_icon),
                tint = colorResource(id = R.color.mainColor),
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
            )

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(5f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = traineeName,
                        fontSize = 18.sp
                    )
                }
                space(h = 5)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.cf_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                    )
                    Text(
                        text = traineeHandle,
                        color = colorResource(id = cf_color.value),
                        fontSize = 15.sp,
                        modifier = Modifier.padding(start = 2.dp)

                    )
                }
            }
        }
    }
}
@Composable
fun searchAboutTrainee() {

    val selectedTrainee=viewModelHelper.selectedTrainee.collectAsState()
    Box(
        modifier= Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp)
    ){
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color.White, RoundedCornerShape(30.dp, 30.dp, 30.dp, 30.dp)),
            shape = RoundedCornerShape(30.dp, 30.dp, 30.dp,30.dp),
            value = selectedTrainee.value,
            onValueChange = { viewModelHelper.setTrainee(it)},
            placeholder = {
                Text(
                    text = stringResource(R.string.search),
                    color=Color.White
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = colorResource(id = R.color.mainColor),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.White,
                disabledIndicatorColor = Color.Transparent,
                disabledTextColor = Color.Transparent

            ),
            textStyle = LocalTextStyle.current.copy(
                color = Color.White
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint=Color.White
                )
            }
        )
    }

}
@Composable
fun sadNews(
    message:MutableState<String>
){

    LazyColumn(
        modifier=Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        item{
            Icon(
                painter = painterResource(R.drawable.sad),
                contentDescription =null,
                tint=MaterialTheme.colors.onSurface,
                modifier = Modifier.size(48.dp)
            )
            space(10)
            Text(
                text=message.value,
                color=MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(20.dp)
            )
        }

    }
}