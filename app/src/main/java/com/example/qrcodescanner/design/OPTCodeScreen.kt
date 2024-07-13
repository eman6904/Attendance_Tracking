package com.example.qrcodescanner.design

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.qrcodescanner.R
import com.example.qrcodescanner.ScreensRoute
import com.example.qrcodescanner.coding.functions.checkOtp
import java.net.URLEncoder


@Composable
fun OTPCode(navController: NavHostController, email: String) {

    val showProgress = remember { mutableStateOf(false) }
    val otpDigits = ArrayList<MutableState<String>>()
    otpDigits.add(remember { mutableStateOf("") })
    otpDigits.add(remember { mutableStateOf("") })
    otpDigits.add(remember { mutableStateOf("") })
    otpDigits.add(remember { mutableStateOf("") })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp, start = 20.dp, end = 20.dp)

    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f),
            contentAlignment = Alignment.Center
        ) {
            Column(

            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    tint = colorResource(id = R.color.mainColor),
                    contentDescription = null,
                )
                Image(
                    painterResource(R.drawable.otp),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "",
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.7f),
        ) {
            space(h = 15)
            Text(
                text = "Enter OTP",
                fontSize = 25.sp,
                fontFamily = FontFamily(Font(R.font.bold2)),
            )
            space(h = 15)
            Text(
                text = "4 digits code has been sent to :",
                color = Color.Gray,
                fontSize = 20.sp
            )
            Text(
                text = email,
                color = Color.Gray,
                fontSize = 18.sp
            )
        }
        space(h = 20)
        Row(
            //  horizontalArrangement = Arrangement.Center,
            //  verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .weight(0.5f)

        ) {
            val modifier = Modifier
                .weight(1f)
            for (i in 0..3) {

                OTPDigit(modifier = modifier, digit = otpDigits[i])
                Spacer(modifier = Modifier.width(15.dp))
            }

        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            space(h = 25)
            confirmButton(
                otpDigits,
                email,
                showProgress,
                navController
            )
            progressBar(show = showProgress)
        }
    }
}

@Composable
fun confirmButton(
    otpDigits: ArrayList<MutableState<String>>,
    email: String,
    showProgress: MutableState<Boolean>,
    navController: NavHostController
) {

    val otpNumber = remember { mutableStateOf<Int>(0) }
    val message = remember { mutableStateOf(listOf<String>()) }
    val token2 = remember { mutableStateOf("eman") }
    val isSuccess = remember { mutableStateOf(false) }
    val shutDown = remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current

    if(message.value.isNotEmpty()) {
        shutDown.value = true
        showProgress.value=false
    }
    if (shutDown.value)
        checkOtpDialog(
            shutDown = shutDown,
            message = message,
            isSuccess = isSuccess,
            navController = navController,
            email = email,
            token2 = token2.value
        )
    Log.d("tokeeen", token2.value.toString())
    Button(
        onClick = {
            calcOtpNumber(otpDigits, otpNumber)
            Log.d("tokeeen", token2.value.toString())
            checkOtp(email, otpNumber.value, isSuccess, message, lifecycleOwner,token2)
            showProgress.value = true
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.mainColor)
        ),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp)
    ) {

        Text(
            text = "Confirm",
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.bold2)),
            color = Color.White,
            modifier = Modifier.padding(5.dp)
        )

    }
}

@Composable
fun OTPDigit(digit: MutableState<String>, modifier: Modifier) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
        elevation = 3.dp,
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = digit.value,
                onValueChange = { digit.value = it },
                placeholder = {
                    Text(
                        text = " * ",
                        textAlign = TextAlign.Center, // Center the placeholder text
                        fontSize = 25.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Gray,
                    disabledIndicatorColor = Color.Transparent,
                    disabledTextColor = Color.Transparent
                ),
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 25.sp,
                    color = Color.Gray
                ), // Center the input text
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

        }
    }
}

fun calcOtpNumber(
    otpDigits: ArrayList<MutableState<String>>,
    otpNumber: MutableState<Int>
) {

    otpNumber.value=0
    for (i in 0..3) {
        otpNumber.value *= 10
        if (otpDigits[i].value.isNotEmpty())
            otpNumber.value += (otpDigits[i].value.toInt())
    }
}

@Composable
fun checkOtpDialog(
    shutDown: MutableState<Boolean>,
    message: MutableState<List<String>>,
    isSuccess: MutableState<Boolean>,
    navController: NavHostController,
    email: String,
    token2: String
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
                        .padding(start = 15.dp, end = 15.dp, top = 15.dp, bottom = 5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        for(item in message.value){
                            Text(
                                text = item,
                            )
                            space(h = 10)
                        }
                    }
                    space(h = 13)
                    Button(
                        onClick = {
                            shutDown.value = false
                            message.value= listOf()
                            val encodedEmail = URLEncoder.encode(email, "UTF-8")
                            val encodedToken = URLEncoder.encode(token2, "UTF-8")
                            if (isSuccess.value)
                                navController.navigate(ScreensRoute.NewPasswordScreen.route + "/$encodedEmail" + "/$encodedToken")
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.mainColor)
                        ), modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "OK",
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}