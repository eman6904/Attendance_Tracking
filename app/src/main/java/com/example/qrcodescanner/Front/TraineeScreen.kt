package com.example.qrcodescanner.Front

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.qrcodescanner.Back.DataClasses.AttendanceResponse
import com.example.qrcodescanner.Back.functions.getMode
import com.example.qrcodescanner.R
import com.example.qrcodescanner.ScreensRoute

@Composable
fun traineeScreen(
    navController: NavHostController,
    apiResponse: AttendanceResponse
){
   Box(
       modifier = Modifier
           .fillMaxSize()
           .background(MaterialTheme.colors.surface)
   ){
       Box(
           modifier = Modifier
               .fillMaxWidth()
               .height(200.dp)
               .background(colorResource(id = R.color.mainColor))
       ){}
       response(
           api_response=apiResponse,
           navController = navController
       )
       Box(
           modifier = Modifier
               .fillMaxSize()
               .padding(20.dp),
           contentAlignment = Alignment.BottomEnd
       ){
           FloatingActionButton(
               onClick = {
                         navController.navigate(ScreensRoute.AttendanceScreen.route)
               },
               shape = CircleShape,
               backgroundColor = colorResource(id = R.color.mainColor),
               modifier = Modifier.size(40.dp)
           ) {
               Icon(
                   imageVector = Icons.Default.People,
                   tint = Color.White,
                   contentDescription = null
               )
           }
       }
   }
}
@Composable
fun response(
    api_response: AttendanceResponse,
    navController: NavHostController
){

    val buttonColor = remember { mutableStateOf(R.color.cf_color_inL) }
    if (getMode() == "Light Mode")
        buttonColor.value = R.color.cf_color_inN
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
            .clip(RoundedCornerShape(10.dp, 10.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Card(
            modifier = Modifier
                .padding(15.dp)
                .weight(2f)
            ,
            shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),

            ) {
            if(api_response.data?.imageUrl==null) {
                Icon(
                    painterResource(R.drawable.profile_photo),
                    tint = colorResource(id = buttonColor.value),
                    modifier = Modifier
                        .fillMaxSize(),
                    contentDescription = "",
                )
            }else{
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = api_response.data.imageUrl,
                    contentDescription = null
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
        verticalArrangement = Arrangement.Center,

        ){
            if(api_response.data!=null)
            rowItem(title = "Name", body = api_response.data.name)

            if(api_response.isSuccess) {
                rowItem(title = "Case", body = "Success")
                rowItem(title = "Result", body = "trainee has been added to attendance")
            }
            else {
                rowItem(title = "Case", body = "Failed")
                rowItem(title = "Result", body = api_response.message)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ){
            Button(
                onClick = { navController.navigate(ScreensRoute.ScannerScreen.route)},
                colors=ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.mainColor)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text="Scan",
                    color= Color.White,
                    fontFamily = FontFamily(Font(R.font.bold2)),
                    modifier = Modifier.padding(3.dp)
                )
            }
            space(h = 15)
            Button(
                onClick = {navController.navigate(ScreensRoute.MainScreen.route)},
                colors=ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = buttonColor.value)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text="Home",
                    color=Color.White,
                    fontFamily = FontFamily(Font(R.font.bold2)),
                   modifier=Modifier.padding(3.dp),
                )
            }
        }
    }
}
@Composable
fun rowItem(
    title:String,
    body:String
){
    val titleColor = remember { mutableStateOf(R.color.cf_color_inL) }
    if (getMode() == "Light Mode")
        titleColor.value = R.color.cf_color_inN
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp)
    ){
        Text(
            text=title+": ",
            color= colorResource(id = titleColor.value)
        )
        Text(
            text=body,
            color=MaterialTheme.colors.onSurface
        )
    }
}