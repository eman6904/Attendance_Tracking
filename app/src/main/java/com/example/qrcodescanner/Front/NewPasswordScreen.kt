package com.example.qrcodescanner.Front

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.qrcodescanner.R
import com.example.qrcodescanner.ScreensRoute
import com.example.qrcodescanner.Back.DataClasses.PasswordResetRequirements
import com.example.qrcodescanner.Back.functions.errorDialog
import com.example.qrcodescanner.Back.functions.resetPassword

@Composable
fun newPassword(
    navController: NavHostController,
    email: String,
    token: String
) {
    Log.d("email", email)
    Log.d("token", token)
    val confirmPassword = rememberSaveable() { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    var emptyPassword = rememberSaveable() { mutableStateOf(false) }
    var notEmptyPass = rememberSaveable() { mutableStateOf(false) }
    var emptyConfirmPassword = rememberSaveable() { mutableStateOf(false) }
    var notEmptyConfirmPass = rememberSaveable() { mutableStateOf(false) }
    var showProgress = rememberSaveable() { mutableStateOf(false) }
    var notSame = rememberSaveable() { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }
    val shutDownError = remember { mutableStateOf(false) }


    val modifierForEmptyField = Modifier
        .fillMaxWidth()
        .border(2.dp, Color.Red, RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp))
    val modifierForNotEmptyField = Modifier
        .fillMaxWidth()


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
                    .weight(3f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painterResource(R.drawable.resetpassword),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "",
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                Text(
                    text = "Reset",
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.bold2)),
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    text = "Password",
                    fontSize = 25.sp,
                    fontFamily = FontFamily(Font(R.font.bold2)),
                    color = MaterialTheme.colors.onSurface
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (emptyPassword.value == false || password.value.isNotEmpty())
                    newPasswordField(password, modifierForNotEmptyField)
                else
                    newPasswordField(password, modifierForEmptyField)
            }
            space(15)
            Box(
                modifier = Modifier
                    .weight(1.5f)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (emptyConfirmPassword.value == false || confirmPassword.value.isNotEmpty()) {

                    Column() {
                        conFirmPasswordField(confirmPassword, modifierForNotEmptyField)
                        if (notSame.value)
                            Text(
                                text = "Doesn't match with password",
                                color = Color.Red,
                                modifier = Modifier
                                    .padding(top = 3.dp)
                                    .fillMaxWidth()
                            )
                    }
                } else
                    conFirmPasswordField(confirmPassword, modifierForEmptyField)
            }
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column() {
                    resetButton(
                        password,
                        confirmPassword,
                        emptyPassword,
                        emptyConfirmPassword,
                        showProgress,
                        notSame,
                        email,
                        token,
                        navController,
                        shutDownError,
                        errorMessage
                    )

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        space(h = 30)
                        progressBar(show = showProgress)
                    }
                }
            }
        }
    }
}

@Composable
fun newPasswordField(password: MutableState<String>, modifier: Modifier) {

    var passwordVisible = rememberSaveable { mutableStateOf(false) }
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
        elevation = 3.dp,
        backgroundColor = MaterialTheme.colors.secondary,
    ) {
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            placeholder = {
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Password")
                }
            },
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colors.onSurface
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.secondary,
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
fun conFirmPasswordField(password: MutableState<String>, modifier: Modifier) {

    Card(
        modifier = modifier.padding(bottom = 10.dp),
        shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
        elevation = 3.dp,
        backgroundColor = MaterialTheme.colors.secondary,
    ) {
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            placeholder = {
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Confirm Password")
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.secondary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Gray,
                disabledIndicatorColor = Color.Transparent,
                disabledTextColor = Color.Transparent

            ),
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colors.onSurface
            ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                )
            },
        )

    }
}

@Composable
fun resetButton(
    password: MutableState<String>,
    confirmPassword: MutableState<String>,
    emptyPassword: MutableState<Boolean>,
    emptyConfPassword: MutableState<Boolean>,
    showProgress: MutableState<Boolean>,
    notSame: MutableState<Boolean>,
    email: String,
    token: String,
    navController: NavHostController,
    shutDownError: MutableState<Boolean>,
    errorMessage: MutableState<String>
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val message = remember { mutableStateOf(listOf<String>()) }
    val isSuccess = remember { mutableStateOf(false) }
    val shutDown = remember { mutableStateOf(false) }

    if (message.value.isNotEmpty()) {
        shutDown.value = true
        showProgress.value = false
    }
    if (shutDown.value) {

        checkPasswordDialog(
            shutDown = shutDown,
            message = message,
            isSuccess = isSuccess,
            navController = navController
        )
    }
    Button(
        onClick = {

            if (password.value.isNotEmpty() &&
                confirmPassword.value.isNotEmpty() &&
                password.value == confirmPassword.value
            ) {

                resetPassword(
                    PasswordResetRequirements(
                        password.value,
                        token,
                        email
                    ),
                    isSuccess,
                    message,
                    shutDownError,
                    errorMessage,
                    showProgress
                )
                showProgress.value = true
            }
            emptyPassword.value = (password.value.isEmpty())
            emptyConfPassword.value = (confirmPassword.value.isEmpty())
            notSame.value = (password.value != confirmPassword.value)
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.mainColor)
        ),
        modifier = Modifier
            .fillMaxWidth(), shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp)
    ) {
        Text(
            text = "Reset",
            color = Color.White,
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.bold2)),
            modifier = Modifier.padding(3.dp)
        )
    }
}

@Composable
fun checkPasswordDialog(
    shutDown: MutableState<Boolean>,
    message: MutableState<List<String>>,
    isSuccess: MutableState<Boolean>,
    navController: NavHostController
) {
    if (shutDown.value) {
        Dialog(
            onDismissRequest = {
                shutDown.value = false
                message.value = listOf()
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
                    if (isSuccess.value) {
                        Text(
                            text = "Success!",
                            modifier = Modifier.weight(1f),
                            fontSize = 22.sp,
                            fontFamily = FontFamily(Font(R.font.bold2)),
                            color = colorResource(id = R.color.mainColor)
                        )
                    } else {
                        Text(
                            text = "Failed!",
                            modifier = Modifier.weight(1f),
                            fontSize = 22.sp,
                            fontFamily = FontFamily(Font(R.font.bold2)),
                            color = colorResource(id = R.color.mainColor)
                        )
                    }
                    space(h = 10)
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        for (item in message.value) {
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
                            message.value = listOf()
                            if (isSuccess.value)
                                navController.navigate(ScreensRoute.MainScreen.route)
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