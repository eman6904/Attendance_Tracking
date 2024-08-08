package com.example.qrcodescanner.front.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.qrcodescanner.R
import com.example.qrcodescanner.front.components.AuthTextField
import com.example.qrcodescanner.front.components.errorDialog
import com.example.qrcodescanner.front.components.progressBar
import com.example.qrcodescanner.front.components.resetPasswordButton
import com.example.qrcodescanner.front.components.space

@Composable
fun ResetPasswordScreen(
    navController: NavHostController,
    email: String,
    token: String
) {

    val confirmPassword = rememberSaveable() { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordError = rememberSaveable { mutableStateOf("") }
    var isPasswordError = rememberSaveable() { mutableStateOf(false) }
    var isConfirmPasswordError = rememberSaveable() { mutableStateOf(false) }
    var confirmPasswordError = rememberSaveable() { mutableStateOf("") }
    var showProgress = rememberSaveable() { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }
    val shutDownError = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                tint = colorResource(id = R.color.mainColor),
                contentDescription = null,
                modifier = Modifier.clickable {
                    navController.popBackStack()
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp)
                .background(MaterialTheme.colors.surface)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            errorDialog(
                shutDown = shutDownError,
                errorMessage = errorMessage
            )

            Image(
                painterResource(R.drawable.resetpassword),
                modifier = Modifier
                    .fillMaxSize()
                    .size(250.dp),
                contentDescription = "",
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.reset),
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.bold2)),
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    text = stringResource(id = R.string.password),
                    fontSize = 25.sp,
                    fontFamily = FontFamily(Font(R.font.bold2)),
                    color = MaterialTheme.colors.onSurface
                )
            }
            space(h = 15)
            AuthTextField(
                placeHolder = stringResource(id = R.string.password),
                textFieldValue = password,
                isError = isPasswordError,
                errorValue = passwordError,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
            space(15)
            AuthTextField(
                placeHolder = stringResource(R.string.confirm_password),
                textFieldValue = confirmPassword,
                isError = isConfirmPasswordError,
                errorValue = confirmPasswordError,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
            space(h = 15)
            resetPasswordButton(
                password,
                confirmPassword,
                isPasswordError,
                passwordError,
                isConfirmPasswordError,
                confirmPasswordError,
                showProgress,
                email,
                token,
                navController,
                shutDownError,
                errorMessage
            )
            space(h = 10)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp),
                contentAlignment = Alignment.Center
            ) {
                progressBar(show = showProgress)
            }
        }
    }
}
