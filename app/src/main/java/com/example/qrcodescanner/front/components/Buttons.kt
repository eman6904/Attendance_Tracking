package com.example.qrcodescanner.front.components

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
import com.example.qrcodescanner.back.Model.ExtraPointRequirements
import com.example.qrcodescanner.back.Model.PasswordResetRequirements
import com.example.qrcodescanner.back.function.getCurrentCamp
import com.example.qrcodescanner.front.screens.calcOtpNumber
import com.example.qrcodescanner.front.utils.checkConfirmPassword
import com.example.qrcodescanner.front.utils.checkEmail
import com.example.qrcodescanner.front.utils.checkPassword
import com.example.qrcodescanner.navigation.ScreensRoute
import com.example.qrcodescanner.R
import com.example.qrcodescanner.back.API.checkOtp
import com.example.qrcodescanner.back.API.forgotPassword
import com.example.qrcodescanner.back.API.resetPassword
import com.example.qrcodescanner.back.API.traineePointsUpdate

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
    points: MutableState<Long>,
    pointsAction: String,
    onItemSelected: (pointsAction: String) -> Unit,
    pointss: MutableState<String>,
    searchedTrainee: MutableState<String>,
    showSending: MutableState<Boolean>,
    shutDownError: MutableState<Boolean>,
    errorMessage: MutableState<String>
) {

    if (pointsAction == stringResource(id = R.string.minus) && points.value > 0)
        points.value *= -1

    val showMessage = remember { mutableStateOf(false) }
    val isSuccess = remember { mutableStateOf(false) }
    val message = remember { mutableStateOf("") }
    val points_action=stringResource(id = R.string.points_action)

    if (message.value.isNotEmpty()) {
        showMessage.value = true
        showSending.value = false

        if (isSuccess.value) {
            onItemSelected(stringResource(id = R.string.points_action))
            pointss.value = ""
            searchedTrainee.value = ""
        }
    }
    if (showMessage.value)
        updatePointsResponse(showMessage = showMessage, isSuccess = isSuccess, message)

    Card(
        modifier = Modifier
            .clickable {

                if (searchedTrainee.value.isNotEmpty() && pointss.value.isNotEmpty() && pointsAction != points_action) {
                    traineePointsUpdate(
                        extraPoint = ExtraPointRequirements(traineeId.value, points.value),
                        isSuccess = isSuccess,
                        message = message,
                        shutDownError = shutDownError,
                        errorMessage = errorMessage
                    )
                    showSending.value = true

                    traineeNameRequire.value = (searchedTrainee.value.isEmpty())
                    pointsRequired.value = (pointss.value.isEmpty())
                    pointActionRequired.value = (pointsAction == points_action)


                } else {
                    traineeNameRequire.value = (searchedTrainee.value.isEmpty())
                    pointsRequired.value = (pointss.value.isEmpty())
                    pointActionRequired.value = (pointsAction == points_action)
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
    email: MutableState<String>,
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
            if (!checkEmail(email=email,isEmailError=isEmailError,emailError=emailError,context)) {
                forgotPassword(
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
    password: MutableState<String>,
    confirmPassword: MutableState<String>,
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
                checkPassword(password=password, isPassError =isPasswordError, passError =passwordError,context=context)
            val checkConfirmPasswordResult= checkConfirmPassword(confirmPassword = confirmPassword, password = password, isConfirmPassError = isConfirmPasswordError,
                confirmPassError = confirmPasswordError,context=context)
            if (!checkPasswordResult&&!checkConfirmPasswordResult) {
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
            checkOtp(
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