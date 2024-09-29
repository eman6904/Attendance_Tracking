package com.example.qrcodescanner.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.qrcodescanner.MainActivity.Companion.apiViewModel
import com.example.qrcodescanner.MainActivity.Companion.viewModelHelper
import com.example.qrcodescanner.data.model.ExtraPointRequirements
import com.example.qrcodescanner.data.model.PasswordResetRequirements
import com.example.qrcodescanner.data.utils.getCurrentCamp
import com.example.qrcodescanner.ui.screens.calcOtpNumber
import com.example.qrcodescanner.ui.utils.checkConfirmPassword
import com.example.qrcodescanner.ui.utils.checkEmail
import com.example.qrcodescanner.ui.utils.checkPassword
import com.example.qrcodescanner.navigation.ScreensRoute
import com.example.qrcodescanner.R
import com.example.qrcodescanner.data.apis.ApiViewModel


@Composable
fun mainButtons(
    btnName: String,
    navController: NavHostController,
    showReminder: MutableState<Boolean>,
    currentCampName: MutableState<String>
) {

    var selectCampDialog = remember { mutableStateOf(false) }
    if (selectCampDialog.value) {

        Dialog(
            onDismissRequest = { selectCampDialog.value = false }
        ) {
            Card(
                modifier = Modifier
                    .width(300.dp)
                    .height(450.dp), shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                elevation = 10.dp
            ) {

                Column() {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .background(colorResource(id = R.color.mainColor)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.camps),
                            color = Color.White,
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.bold2))
                        )
                    }
                    selectCamp(selectCampDialog, currentCampName)
                }
            }
        }
    }
    val extraPoints= stringResource(id =R.string.extra_points )
    val viewAttendance= stringResource(id =R.string.view_attendance )
    Button(
        onClick = {
            if (btnName == extraPoints) {

                if (getCurrentCamp() == null) {
                    showReminder.value = true
                } else {
                    navController.navigate(ScreensRoute.ExtraPointScreen.route)
                }
            } else if (btnName == viewAttendance) {
                if (getCurrentCamp() == null) {
                    showReminder.value = true
                } else {
                    navController.navigate(ScreensRoute.AttendanceScreen.route)
                }
            } else
                selectCampDialog.value = true


        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp, 0.dp, 30.dp, 0.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.mainColor),
            contentColor = Color.White
        )
    ) {
        Text(
            text = btnName, modifier = Modifier.padding(10.dp),
            fontFamily = FontFamily(Font(R.font.bold2)),
            fontSize = 17.sp
        )
    }
}
@Composable
fun scanQrCodeButton(
    navController: NavHostController,
    showReminder: MutableState<Boolean>,
) {

    Button(
        onClick = {
            if (getCurrentCamp() != null) {
                navController.navigate(ScreensRoute.ScannerScreen.route)
            } else {
                showReminder.value = true
            }
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp, 0.dp, 30.dp, 0.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.mainColor),
            contentColor = Color.White
        )
    ) {
        Text(
            text = stringResource(R.string.scan_qrcode), modifier = Modifier.padding(10.dp),
            fontFamily = FontFamily(Font(R.font.bold2)),
            fontSize = 17.sp
        )
    }
}
@Composable
fun updatePointsButton(
    traineeNameRequire: MutableState<Boolean>,
    pointsRequired: MutableState<Boolean>,
    pointActionRequired: MutableState<Boolean>,
    traineeId: MutableState<String>,
    showSending: MutableState<Boolean>,
    shutDownError: MutableState<Boolean>,
    errorMessage: MutableState<String>,
    pointsError: MutableState<String>,
) {

    val points= viewModelHelper.traineePoints.collectAsState()
    val pointsString= viewModelHelper.pointsString.collectAsState()
    val pointsAction= viewModelHelper.pointAction.collectAsState()
    val searchedTrainee= viewModelHelper.searchedTrainee.collectAsState()
    if (pointsAction.value == stringResource(id = R.string.minus) && points.value > 0)
       viewModelHelper.setPoint( points.value * -1)

    val showMessage = remember { mutableStateOf(false) }
    val isSuccess = remember { mutableStateOf(false) }
    val message = remember { mutableStateOf("") }
    val points_action=stringResource(id = R.string.points_action)
    val msg= stringResource(id = R.string.this_field_is_required)


    if (message.value.isNotEmpty()) {
        showMessage.value = true
        showSending.value = false

    }
    if (showMessage.value)
        updatePointsResponse(showMessage = showMessage, isSuccess = isSuccess, message)

    Card(
        modifier = Modifier
            .clickable {
                if (searchedTrainee.value.isNotEmpty() && pointsString.value.isNotEmpty() && pointsAction.value != points_action) {
                    apiViewModel.traineePointsUpdate(
                        extraPoint = ExtraPointRequirements(traineeId.value, points.value),
                        isSuccess = isSuccess,
                        message = message,
                        shutDownError = shutDownError,
                        errorMessage = errorMessage
                    )
                    showSending.value = true

                    traineeNameRequire.value = (searchedTrainee.value.isEmpty())
                    pointsRequired.value = (pointsString.value.isEmpty())
                    pointActionRequired.value = (pointsAction.value == points_action)


                } else {
                    traineeNameRequire.value = (searchedTrainee.value.isEmpty())
                    pointsRequired.value = (pointsString.value.isEmpty())
                    if (pointsRequired.value)
                        pointsError.value =msg
                    pointActionRequired.value = (pointsAction.value == points_action)
                }

            }
            .fillMaxWidth()
            .padding(15.dp),
        backgroundColor = colorResource(id = R.color.mainColor2),
        shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
        elevation = 15.dp

    ) {
        Text(
            text = stringResource(R.string.done),
            modifier = Modifier.padding(15.dp),
            color = Color.White,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.bold2))
        )
    }
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun sendOtpCodeButton(
    isEmailError: MutableState<Boolean>,
    emailError: MutableState<String>,
    navController: NavHostController,
    showProgress: MutableState<Boolean>,
    shutDownError: MutableState<Boolean>,
    errorMessage: MutableState<String>
) {

    val message = remember { mutableStateOf(listOf<String>()) }
    val isSuccess = remember { mutableStateOf(false) }
    val shutDown = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context= LocalContext.current
    val email = viewModelHelper.email.collectAsState()

    if (message.value.isNotEmpty()) {
        shutDown.value = true
        showProgress.value = false
    }

    if (shutDown.value)
        checkEmailResponse(
            shutDown = shutDown,
            message = message,
            isSuccess = isSuccess,
            navController = navController,
            email = email.value
        )

    Button(
        onClick = {
            keyboardController?.hide()
            if (!checkEmail(email=email.value,isEmailError=isEmailError,emailError=emailError,context)) {
                apiViewModel.forgotPassword(
                    email.value,
                    isSuccess,
                    message,
                    shutDownError,
                    errorMessage,
                    showProgress
                )
                showProgress.value = true
            }
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.mainColor)
        ),
        modifier = Modifier
            .fillMaxWidth(), shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp)
    ) {
        Text(
            text = stringResource(R.string.send),
            color = Color.White,
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.bold2)),
            modifier = Modifier.padding(3.dp)
        )
    }
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun resetPasswordButton(
    isPasswordError: MutableState<Boolean>,
    passwordError: MutableState<String>,
    isConfirmPasswordError: MutableState<Boolean>,
    confirmPasswordError: MutableState<String>,
    showProgress: MutableState<Boolean>,
    email: String,
    token: String,
    navController: NavHostController,
    shutDownError: MutableState<Boolean>,
    errorMessage: MutableState<String>
) {
    val message = remember { mutableStateOf(listOf<String>()) }
    val isSuccess = remember { mutableStateOf(false) }
    val shutDown = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context= LocalContext.current
    val password= viewModelHelper.newPassword.collectAsState()
    val passConfirm= viewModelHelper.passwordConfirm.collectAsState()

    if (message.value.isNotEmpty()) {
        shutDown.value = true
        showProgress.value = false
    }
    if (shutDown.value) {

        checkPasswordResponse(
            shutDown = shutDown,
            message = message,
            isSuccess = isSuccess,
            navController = navController,
            shutDownError=shutDownError,
            errorMessage=errorMessage
        )
    }
    Button(
        onClick = {
            keyboardController?.hide()
            val checkPasswordResult=
                checkPassword(password=password.value, isPassError =isPasswordError, passError =passwordError,context=context)
            val checkConfirmPasswordResult= checkConfirmPassword(confirmPassword = passConfirm.value, password = password.value, isConfirmPassError = isConfirmPasswordError,
                confirmPassError = confirmPasswordError,context=context)
            if (!checkPasswordResult&&!checkConfirmPasswordResult) {
                apiViewModel. resetPassword(
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
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.mainColor)
        ),
        modifier = Modifier
            .fillMaxWidth(), shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp)
    ) {
        Text(
            text = stringResource(R.string.reset),
            color = Color.White,
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.bold2)),
            modifier = Modifier.padding(3.dp)
        )
    }
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun confirmOtpCodeButton(
    otpDigits: ArrayList<MutableState<String>>,
    email: String,
    showProgress: MutableState<Boolean>,
    navController: NavHostController,
    shutDownError: MutableState<Boolean>,
    errorMessage: MutableState<String>
) {

    val otpNumber = remember { mutableStateOf<Int>(0) }
    val message = remember { mutableStateOf(listOf<String>()) }
    val token2 = remember { mutableStateOf("eman") }
    val isSuccess = remember { mutableStateOf(false) }
    val shutDown = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val viewModel=ApiViewModel()

    if (message.value.isNotEmpty()) {
        shutDown.value = true
        showProgress.value = false
    }
    if (shutDown.value)
        checkOtpCodeResponse(
            shutDown = shutDown,
            message = message,
            isSuccess = isSuccess,
            navController = navController,
            email = email,
            token2 = token2.value
        )

    Button(
        onClick = {

            keyboardController?.hide()
            calcOtpNumber(otpDigits, otpNumber)
            viewModel.checkOtp(
                email,
                otpNumber.value,
                isSuccess,
                message,
                token2,
                shutDownError,
                errorMessage,
                showProgress
            )
            showProgress.value = true
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.mainColor)
        ),
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp)
    ) {

        Text(
            text = stringResource(R.string.confirm),
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.bold2)),
            color = Color.White,
            modifier = Modifier.padding(5.dp)
        )

    }
}