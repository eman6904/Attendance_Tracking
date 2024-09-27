package com.example.qrcodescanner.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.qrcodescanner.data.utils.getCurrentCamp
//import com.example.qrcodescanner.ui.screens.searchAboutTrainee
import com.example.qrcodescanner.R

@Composable
fun AttendanceTopBar(
    navController: NavHostController,
    selectedTrainee: MutableState<String>
) {

    Card(
        modifier = Modifier
            .height(160.dp)
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

       Column(){
           Row(
               modifier = Modifier.fillMaxSize()
                   .padding(15.dp)
                   .weight(1f),
               horizontalArrangement = Arrangement.Center
           ){
               Icon(
                   imageVector = Icons.Default.ArrowBack,
                   contentDescription = null,
                   tint = Color.White,
                   modifier = Modifier.clickable {
                       navController.navigateUp()
                   }.align(Alignment.CenterVertically)
               )
               Box(
                   modifier=Modifier.fillMaxSize(),
                   contentAlignment = Alignment.CenterStart
               ){
                   Text(
                       text= getCurrentCamp()!!.name+" Camp",
                       modifier = Modifier.padding(start=10.dp),
                       color=Color.White,
                       fontFamily = FontFamily(Font(R.font.bold2)),
                   )
               }
           }
           Box(
               modifier = Modifier
                   .weight(1f)
                   .fillMaxSize(),
               contentAlignment = Alignment.TopCenter
           ) {
              // searchAboutTrainee(selectedTrainee = selectedTrainee)
           }

       }
    }
}