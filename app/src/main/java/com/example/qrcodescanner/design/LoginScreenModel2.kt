package com.example.qrcodescanner.design

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.qrcodescanner.R
import com.example.qrcodescanner.ScreensRoute

@Composable
fun login2(navController: NavHostController){
    val userName = rememberSaveable() { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    var emptyPassword = rememberSaveable() { mutableStateOf(false) }
    var emptyUserName = rememberSaveable() { mutableStateOf(false) }
    val modifierForEmptyField = Modifier
        .fillMaxSize()
        .padding(start = 20.dp, end = 20.dp)
        .border(2.dp, Color.Red, RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp))
    val modifierForNotEmptyField = Modifier
        .fillMaxSize()
        .padding(start = 20.dp, end = 20.dp)

        Column(
            modifier= Modifier
                .fillMaxSize()
        ){
            Box(
                modifier= Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(colorResource(id = R.color.mainColor)),
                contentAlignment = Alignment.BottomCenter
            ){
                Column(

                    modifier = Modifier
                        .padding(bottom =50.dp,start=20.dp,end=30.dp)
                        .fillMaxWidth()
                ){
                   Box(
                       modifier = Modifier.fillMaxSize().weight(2f),
                       contentAlignment = Alignment.BottomStart
                   ){
                       Card(
                           modifier = Modifier,
                           shape =RoundedCornerShape(20.dp,20.dp,20.dp,20.dp),

                           ){
                           Image(
                               painterResource(R.drawable.icpc_yellow_logo),
                               modifier = Modifier
                                   .size(100.dp)
                                   .aspectRatio(1f)
                               ,
                               contentDescription = "",
                           )
                       }
                   }
                    Column(
                        modifier = Modifier.weight(1f).fillMaxSize()
                    ){
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Attendance registration",color=Color.White,
                            fontSize = 20.sp
                            ,  fontFamily = FontFamily(Font(R.font.bold2)),)
                        Text("ICPC sohag Community Trainings",color=Color.White, fontSize = 15.sp)
                        Text("ISc System",color=Color.White, fontSize = 15.sp)
                        //ICPC sohag Community Trainings
                    }
                }
            }
            Card(
             shape= RoundedCornerShape(50.dp,50.dp,0.dp,0.dp),
             modifier= Modifier
                 .weight(1f)
                 .background(colorResource(id = R.color.mainColor))
            ){
                Column(

                    modifier= Modifier
                        .fillMaxSize()
                        .padding(15.dp,top=50.dp)
                ){
                    Box(
                        modifier= Modifier.weight(1f).padding(top=20.dp,bottom=15.dp)
                    ){
                        if(emptyUserName.value==false||userName.value.isNotEmpty())
                            userNameField2(userName,modifierForNotEmptyField)
                        else
                            userNameField2(userName,modifierForEmptyField)
                    }
                    Box(
                        modifier= Modifier.weight(1f).padding(top=20.dp,bottom=15.dp)
                    ){
                        if(emptyPassword.value==false||password.value.isNotEmpty())
                            passwordField2(password,modifierForNotEmptyField)
                        else
                            passwordField2(password,modifierForEmptyField)
                    }
                    Box(
                        modifier= Modifier.weight(1f)
                    ){

                        Button(
                            onClick = {
                                      navController.navigate(ScreensRoute.MainScreen.route)
                            },
                            modifier= Modifier
                                .padding(top=10.dp,start = 20.dp, end = 20.dp)
                                .fillMaxWidth(),
                            shape= RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                            colors= ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(id = R.color.mainColor)
                            )
                        ){
                            Text(text="LOGIN",color= Color.White,modifier= Modifier.padding(10.dp))
                        }
                    }

                }
            }
        }
}@Composable
fun passwordField2(password: MutableState<String>,modifier:Modifier) {

    var passwordVisible = rememberSaveable { mutableStateOf(false) }
    Card(
        modifier=modifier,
        shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
        elevation = 3.dp,
    ) {
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            placeholder = {Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier.fillMaxSize()){
                Text(text = "Password")
            } },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Gray,
                disabledIndicatorColor = Color.Transparent,
                disabledTextColor = Color.Transparent

            ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            visualTransformation =
            if (passwordVisible.value) VisualTransformation.None
            else PasswordVisualTransformation(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color.DarkGray
                )
            },
            trailingIcon = {
                val image = if (passwordVisible.value)
                    Icons.Filled.Visibility
                else
                    Icons.Filled.VisibilityOff

                // Please provide localized description for accessibility services
                val description =
                    if (passwordVisible.value) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible.value = !(passwordVisible.value) }) {
                    Icon(imageVector = image, description)
                }
            }
        )
    }
}
@Composable
fun userNameField2(userName: MutableState<String>, modifier: Modifier) {

    Card(
        modifier =modifier,
        shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
        elevation = 3.dp,
    ) {
        TextField(
            modifier=Modifier,
            value = userName.value,
            onValueChange = { userName.value = it },
            placeholder = {Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier.fillMaxSize()){
                Text(text = "User Name")
            } },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Gray,
                disabledIndicatorColor = Color.Transparent,
                disabledTextColor = Color.Transparent

            ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.DarkGray
                )
            }
        )
    }
}

