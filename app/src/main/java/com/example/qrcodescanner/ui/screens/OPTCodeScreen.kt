package com.example.qrcodescanner.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.qrcodescanner.R
import com.example.qrcodescanner.ui.components.OTPDigit
import com.example.qrcodescanner.ui.components.confirmOtpCodeButton
import com.example.qrcodescanner.ui.components.errorDialog
import com.example.qrcodescanner.ui.components.progressBar
import com.example.qrcodescanner.ui.components.space


@Composable
fun OTPCode(navController: NavHostController, email: String) {

    val showProgress = remember { mutableStateOf(false) }
    val otpDigits = ArrayList<MutableState<String>>()
    otpDigits.add(remember { mutableStateOf("") })
    otpDigits.add(remember { mutableStateOf("") })
    otpDigits.add(remember { mutableStateOf("") })
    otpDigits.add(remember { mutableStateOf("") })
    val errorMessage = remember { mutableStateOf("") }
    val shutDownError = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, start = 20.dp, end = 20.dp)
                .background(MaterialTheme.colors.surface)
                .verticalScroll(rememberScrollState())
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
            Image(
                painterResource(R.drawable.otp),
                modifier = Modifier
                    .fillMaxSize()
                    .size(300.dp),
                contentDescription = "",
            )
            Text(
                text = stringResource(R.string.enter_otp),
                fontSize = 25.sp,
                fontFamily = FontFamily(Font(R.font.bold2)),
                color = MaterialTheme.colors.onSurface
            )
            space(h = 10)
            Text(
                text = stringResource(R.string._4_digits_code_has_been_sent_to),
                fontSize = 18.sp,
                color = MaterialTheme.colors.onSurface
            )
            Text(
                text = email,
                fontSize = 13.sp,
                color = MaterialTheme.colors.onSurface
            )
            space(h = 20)
            Row(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                for (i in 0..3) {

                    OTPDigit(modifier = modifier, digit = otpDigits[i])
                    if (i < 3)
                        Spacer(modifier = Modifier.width(25.dp))
                }

            }
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                space(h = 25)
                confirmOtpCodeButton(
                    otpDigits,
                    email,
                    showProgress,
                    navController,
                    shutDownError,
                    errorMessage
                )
                space(h = 15)
                progressBar(show = showProgress)
            }
        }
    }
}

fun calcOtpNumber(
    otpDigits: ArrayList<MutableState<String>>,
    otpNumber: MutableState<Int>
) {

    otpNumber.value = 0
    for (i in 0..3) {
        otpNumber.value *= 10
        if (otpDigits[i].value.isNotEmpty())
            otpNumber.value += (otpDigits[i].value.toInt())
    }
}