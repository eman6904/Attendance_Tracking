package com.example.qrcodescanner.design

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.navigation.NavHostController
import com.example.qrcodescanner.R
import com.example.qrcodescanner.coding.APIs.ViewModel
import com.example.qrcodescanner.coding.DataClasses.ExtraPoint


@Composable
fun attendanceList(navController: NavHostController){

    val viewModel = ViewModel()
    val token=stringResource(id = R.string.token)
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        topBar(navController = navController)
       Column(
           modifier = Modifier.padding(top=5.dp)
       ){
          // val traineeListState = viewModel.traineeList.collectAsState(initial = emptyList())
           LaunchedEffect(Unit) {
               viewModel.traineePointsUpdate( ExtraPoint("167989fa-b0cd-4f9f-bb56-43ee736007d1",10),"Bearer $token")
           }

           columnItem()
           columnItem()
           columnItem()
           columnItem()
       }
    }
}
@Composable
fun topBar(navController: NavHostController) {

    Card(
        modifier = Modifier
            .height(102.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(
            topEnd = 0.dp,
            topStart = 0.dp,
            bottomEnd = 30.dp,
            bottomStart = 30.dp
        ),
        backgroundColor = colorResource(id = R.color.mainColor),
        elevation = 15.dp
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Attendance List", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription =null,
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
fun columnItem(){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp)
            .height(90.dp),
        elevation = 10.dp
    ) {
        Text("")
        Text("")
    }
    Divider(Modifier.height(3.dp))
}