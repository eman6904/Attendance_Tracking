//package com.example.qrcodescanner.ui.screens
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Email
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.font.Font
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import com.example.qrcodescanner.R
//import com.example.qrcodescanner.ui.components.AuthTextField
//import com.example.qrcodescanner.ui.components.errorDialog
//import com.example.qrcodescanner.ui.components.progressBar
//import com.example.qrcodescanner.ui.components.sendOtpCodeButton
//import com.example.qrcodescanner.ui.components.space
//
//@Composable
//fun ForgotPasswordScreen(navController: NavHostController) {
//
//    val email = rememberSaveable { mutableStateOf("") }
//    var isEmailError = rememberSaveable() { mutableStateOf(false) }
//    var emailError = rememberSaveable() { mutableStateOf("") }
//    var showProgress = rememberSaveable() { mutableStateOf(false) }
//    val errorMessage = remember { mutableStateOf("") }
//    val shutDownError = remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colors.surface)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(top = 10.dp, start = 20.dp, end = 20.dp)
//                .background(MaterialTheme.colors.surface)
//                .verticalScroll(rememberScrollState())
//
//        ) {
//            errorDialog(
//                shutDown = shutDownError,
//                errorMessage = errorMessage
//            )
//
//            Icon(
//                imageVector = Icons.Default.ArrowBack,
//                tint = colorResource(id = R.color.mainColor),
//                contentDescription = null,
//                modifier = Modifier.clickable {
//                    navController.popBackStack()
//                }
//            )
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                contentAlignment = Alignment.Center
//            ) {
//                Image(
//                    painterResource(R.drawable.forget_icon2),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .size(300.dp)
//                        .padding(25.dp),
//                    contentDescription = "",
//                )
//
//            }
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//            ) {
//
//                Text(
//                    text = stringResource(R.string.forgot),
//                    fontSize = 20.sp,
//                    fontFamily = FontFamily(Font(R.font.bold2)),
//                    color = MaterialTheme.colors.onSurface
//                )
//                Text(
//                    text = stringResource(R.string.passwordd),
//                    fontSize = 25.sp,
//                    fontFamily = FontFamily(Font(R.font.bold2)),
//                    color = MaterialTheme.colors.onSurface
//                )
//                space(h = 15)
//                Text(
//                    text = stringResource(R.string.don_t_worry_it_happens_please_enter_email) +
//                            stringResource(R.string.address_associated_with_your_account),
//                    color = MaterialTheme.colors.onSurface,
//                    fontSize = 15.sp
//                )
//                space(h = 20)
//                AuthTextField(
//                    placeHolder = stringResource(R.string.email),
//                    textFieldValue = email,
//                    isError = isEmailError,
//                    errorValue = emailError,
//                    leadingIcon = {
//                        Icon(
//                            imageVector = Icons.Default.Email,
//                            contentDescription = null,
//                        )
//                    },
//                    keyboardOptions = KeyboardOptions.Default.copy(
//                        keyboardType = KeyboardType.Text,
//                        imeAction = ImeAction.Next
//                    ),
//                    modifier = Modifier.fillMaxWidth()
//                )
//                space(h = 15)
//                Column {
//                    sendOtpCodeButton(
//                        email = email,
//                        isEmailError = isEmailError,
//                        navController = navController,
//                        showProgress = showProgress,
//                        shutDownError = shutDownError,
//                        errorMessage = errorMessage,
//                        emailError = emailError
//                    )
//                    space(h = 10)
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(5.dp),
//                        contentAlignment = Alignment.Center
//                    ) { progressBar(show = showProgress) }
//                }
//
//            }
//            space((LocalDensity.current.density * 10).toInt())
//        }
//    }
//}
