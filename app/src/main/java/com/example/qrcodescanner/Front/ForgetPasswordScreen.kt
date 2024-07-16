package com.example.qrcodescanner.Front

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.qrcodescanner.R
import com.example.qrcodescanner.ScreensRoute
import com.example.qrcodescanner.Back.functions.errorDialog
import com.example.qrcodescanner.Back.functions.forgotPassword

@Composable
fun forgetPassword(navController: NavHostController) {

    val email = rememberSaveable { mutableStateOf("") }
    var emptyMail = rememberSaveable() { mutableStateOf(false) }
    var showProgress = rememberSaveable() { mutableStateOf(false) }
    val  errorMessage= remember { mutableStateOf("")}
    val  shutDownError= remember { mutableStateOf(false)}

    val modifierForEmptyField = Modifier
        .fillMaxWidth()
        .border(2.dp, Color.Red, RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp))
    val modifierForNotEmptyField = Modifier
        .fillMaxWidth()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp, start = 20.dp, end = 20.dp)

    ) {
        errorDialog(
            shutDown = shutDownError,
            errorMessage = errorMessage
        )

           Icon(
               imageVector = Icons.Default.ArrowBack,
               tint = colorResource(id = R.color.mainColor),
               contentDescription = null,
               modifier = Modifier.clickable {
                   navController.popBackStack()
               }
           )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painterResource(R.drawable.forget_icon2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(25.dp),
                contentDescription = "",
            )

        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2.3f)
            ) {
                Text(
                    text = "Forgot",
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.bold2)),
                )
                Text(
                    text = "Password?",
                    fontSize = 25.sp,
                    fontFamily = FontFamily(Font(R.font.bold2)),
                )
                space(h = 15)
                Text(
                    text = "Don't worry it happens.Please enter email " +
                            "address associated with your account.",
                    color = Color.Gray,
                    fontSize = 15.sp
                )
            }
            space(h = 10)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
            ) {
                if (emptyMail.value == false || email.value.isNotEmpty())
                    emailField(email, modifierForNotEmptyField)
                else
                    emailField(email, modifierForEmptyField)
            }
            space(h = 10)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2.5f),
            ) {

                Column() {
                    space(h = 15)
                    sendButton(
                        email=email,
                        emptyEmail=emptyMail,
                        navController,
                        showProgress,
                        shutDownError,
                        errorMessage
                    )

                    progressBar(show = showProgress)
                }
            }

        }

    }
}

@Composable
fun emailField(email: MutableState<String>, modifier: Modifier) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
        elevation = 3.dp,
    ) {

        TextField(
            modifier = Modifier,
            value = email.value,
            onValueChange = { email.value = it },
            placeholder = {
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Email")
                }
            },
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
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = Color.DarkGray
                )
            }
        )

    }
}

@Composable
fun sendButton(
email:MutableState<String>,
emptyEmail:MutableState<Boolean>,
navController: NavHostController,
showProgress:MutableState<Boolean>,
shutDownError:MutableState<Boolean>,
errorMessage:MutableState<String>
) {

    val message = remember { mutableStateOf(listOf<String>()) }
    val isSuccess=remember{ mutableStateOf(false)}
    val shutDown=remember{ mutableStateOf(false)}

    if(message.value.isNotEmpty()) {
        shutDown.value = true
        showProgress.value=false
    }

    if(shutDown.value)
        checkEmailDialog(
            shutDown = shutDown,
            message = message,
            isSuccess =isSuccess,
            navController =navController,
            email = email.value
        )

    Button(
        onClick = {
                  if(email.value.isNotEmpty()){
                      forgotPassword(
                          email.value,
                          isSuccess,
                          message,
                          shutDownError,
                          errorMessage,
                          showProgress
                      )
                      showProgress.value=true
                   }else{
                       emptyEmail.value=true
                   }
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.mainColor)
        ),
        modifier = Modifier
            .fillMaxWidth(), shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp)
    ) {
        Text(
            text = "Send",
            color = Color.White,
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.bold2)),
            modifier = Modifier.padding(3.dp)
        )
    }
}
@Composable
fun checkEmailDialog(
    shutDown:MutableState<Boolean>,
    message: MutableState<List<String>>,
    isSuccess:MutableState<Boolean>,
    navController: NavHostController,
    email:String
) {
    if (shutDown.value) {
        Dialog(
            onDismissRequest = {
                shutDown.value = false
                message.value= listOf()
            }) {

            Card(
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp),
                elevation = 10.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 20.dp, end = 15.dp, top = 15.dp, bottom = 5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    space(h = 10)
                    if(isSuccess.value){
                        Text(
                            text = "Success!",
                            modifier = Modifier.weight(1f),
                            fontSize = 22.sp,
                            fontFamily = FontFamily(Font(R.font.bold2)),
                            color= colorResource(id = R.color.mainColor)
                        )
                    }else{
                        Text(
                            text = "Failed!",
                            modifier = Modifier.weight(1f),
                            fontSize = 22.sp,
                            fontFamily = FontFamily(Font(R.font.bold2)),
                            color= colorResource(id = R.color.mainColor)
                        )
                    }
                    space(h = 10)
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        for(item in message.value){
                            Text(
                                text = item,
                            )
                            space(h = 10)
                        }
                    }
                    space(h = 5)
                    Button(
                        onClick = {
                            shutDown.value = false
                            message.value= listOf()
                            if(isSuccess.value)
                                navController.navigate(ScreensRoute.OTPCodeScreen.route+"/$email")
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.mainColor)
                        )
                    , modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "OK",
                            fontSize = 15.sp,
                            color=Color.White
                        )
                    }
                }
            }
        }
    }
}