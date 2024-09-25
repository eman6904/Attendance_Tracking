package com.example.qrcodescanner.ui.screens


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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.qrcodescanner.R
import com.example.qrcodescanner.data.apis.ViewModel
import com.example.qrcodescanner.data.model.ItemData
import com.example.qrcodescanner.data.utils.getMode
import com.example.qrcodescanner.ui.components.AttendanceTopBar
import com.example.qrcodescanner.ui.components.progressBar
import com.example.qrcodescanner.ui.components.space
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@Composable
fun AttendanceScreen(navController: NavHostController) {


    val showProgress = remember { mutableStateOf(false) }
    val itemsCase = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }
    val shutDownError = remember { mutableStateOf(false) }
    val selectedTrainee = remember { mutableStateOf("") }
    var isRefreshing =remember { mutableStateOf(false) }
    val presentTraineeList = remember { mutableStateListOf<ItemData>() }
    val viewModel= ViewModel()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing.value)

        LaunchedEffect(isRefreshing.value,selectedTrainee.value)
        {

               if(isRefreshing.value)
                showProgress.value = false
            else if(presentTraineeList.isEmpty())
                showProgress.value=true
            viewModel.getPresentTrainees(
                trainees = presentTraineeList,
                itemsCase = itemsCase,
                showProgress = showProgress,
                shutDownError = shutDownError,
                errorMessage = errorMessage,
                selectedTrainee = selectedTrainee
            )
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
            selectedTrainee = selectedTrainee
        )
        Column(
            modifier = Modifier
                .padding(top = 5.dp)
                .background(MaterialTheme.colors.surface)

        ) {
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
                    presentTraineeList = presentTraineeList
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
fun searchAboutTrainee(
    selectedTrainee: MutableState<String>
) {

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
               onValueChange = { selectedTrainee.value = it },
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
                color=MaterialTheme.colors.onSurface
            )
        }

    }
}