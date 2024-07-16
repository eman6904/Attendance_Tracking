package com.example.qrcodescanner.Front


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.qrcodescanner.R
import com.example.qrcodescanner.Back.DataClasses.ItemData
import com.example.qrcodescanner.Back.functions.errorDialog
import com.example.qrcodescanner.Back.functions.getCurrentCamp
import com.example.qrcodescanner.Back.functions.getPresentTrainees


@Composable
fun attendanceList(navController: NavHostController) {


    val showProgress = remember { mutableStateOf(false) }
    val presentTraineeList = remember { mutableStateListOf<ItemData>() }
    val itemsCase = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }
    val shutDownError = remember { mutableStateOf(false) }

    LaunchedEffect(Unit)
    {
        getPresentTrainees(
            trainees = presentTraineeList,
            itemsCase = itemsCase,
            showProgress = showProgress,
            shutDownError = shutDownError,
            errorMessage = errorMessage
        )
    }

    showProgress.value = (presentTraineeList.isEmpty())

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        topBar(navController = navController)
        Column(
            modifier = Modifier.padding(top = 5.dp)

        ) {
            errorDialog(
                shutDown = shutDownError,
                errorMessage = errorMessage
            )
            if (itemsCase.value == "No trainee has presented yet" || itemsCase.value == "No current session for now.") {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = itemsCase.value)
                }
            } else {
                progressBar(show = showProgress)
                LazyColumn() {

                    items(presentTraineeList) { trainee ->

                        columnItem(traineeName = trainee.name)
                    }
                }
            }

        }
    }
}

@Composable
fun topBar(navController: NavHostController) {

    Card(
        modifier = Modifier
            .height(110.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(
            topEnd = 0.dp,
            topStart = 0.dp,
            bottomEnd = 15.dp,
            bottomStart = 15.dp
        ),
        backgroundColor = colorResource(id = R.color.mainColor),
        elevation = 15.dp
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = getCurrentCamp()!!.name + " Camp", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    backgroundColor = colorResource(id = R.color.mainColor),
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        ) {}
    }
}

@Composable
fun columnItem(traineeName: String) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp)
            .height(90.dp),
        elevation = 10.dp
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
                // horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(5f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {

                    Text(traineeName)
                }
            }
        }
    }
    Divider(Modifier.height(3.dp))
}
