package com.example.qrcodescanner.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.qrcodescanner.MainActivity.Companion.viewModelHelper
import com.example.qrcodescanner.data.utils.getCurrentUser
import com.example.qrcodescanner.navigation.ScreensRoute
import com.example.qrcodescanner.R
import com.example.qrcodescanner.data.utils.logout
import java.net.URLEncoder

@Composable
fun campReminder(
    showReminder: MutableState<Boolean>
) {
    if (showReminder.value) {

        Dialog(
            onDismissRequest = {
                showReminder.value = false
            }) {

            Card(
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp),
                elevation = 10.dp
            ) {

                Column(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 10.dp)
                        .fillMaxSize(),
                ) {
                    Text(
                        text = "Hi ${getCurrentUser()!!.firstName} 👋",
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.bold2)),
                    )
                    space(5)
                    Text(
                        text = stringResource(R.string.please_don_t_forget_to_select_the_camp)
                    )
                    space(h = 10)
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Button(
                            onClick = {
                                showReminder.value = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(id = R.color.mainColor)
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.ok),
                                fontSize = 12.sp,
                                color = Color.White,
                                fontFamily = FontFamily(Font(R.font.bold2))
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun updatePointsResponse(
    showMessage: MutableState<Boolean>,
    isSuccess: MutableState<Boolean>,
    message: MutableState<String>,
    traineeId:MutableState<String>
) {

    if (showMessage.value) {

        Dialog(
            onDismissRequest = {
                showMessage.value = false
                message.value = ""
            }) {

            Card(
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp),
                elevation = 10.dp
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(15.dp)
                ) {
                    if (isSuccess.value) {
                        Icon(
                            painter = painterResource(R.drawable.done2),
                            contentDescription = null,
                            tint = colorResource(id = R.color.mainColor),
                            modifier = Modifier
                                .weight(1f)
                                .size(90.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = stringResource(R.string.success),
                            modifier = Modifier,
                            fontSize = 25.sp,
                            fontFamily = FontFamily(Font(R.font.bold2))
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = stringResource(R.string.trainee_points_have_been_updated),
                            modifier = Modifier
                        )
                    } else {

                        Text(
                            text = message.value,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        val pointsAction=stringResource(id = R.string.points_action)
                        if (isSuccess.value) {
                            Button(
                                onClick = {
                                    showMessage.value = false
                                    message.value = ""
                                    traineeId.value=""
                                    viewModelHelper.setAction(pointsAction)
                                    viewModelHelper.setPointsString("")
                                   viewModelHelper.settTrainee("")
                                   viewModelHelper.setPoint(0)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = colorResource(id = R.color.mainColor),
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(id = R.string.ok),
                                    fontSize = 15.sp,
                                    color = Color.White,
                                    fontFamily = FontFamily(Font(R.font.bold2))
                                )
                            }
                        } else {
                            Button(
                                onClick = {
                                    viewModelHelper.setAction(pointsAction)
                                    viewModelHelper.setPointsString("")
                                    viewModelHelper.settTrainee("")
                                    viewModelHelper.setPoint(0)
                                    showMessage.value = false
                                    message.value = ""
                                    traineeId.value=""
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.Red
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(id = R.string.ok),
                                    fontSize = 15.sp,
                                    color = Color.White,
                                    fontFamily = FontFamily(Font(R.font.bold2))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun checkEmailResponse(
    shutDown: MutableState<Boolean>,
    message: MutableState<List<String>>,
    isSuccess: MutableState<Boolean>,
    navController: NavHostController,
    email: String
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
                            text = stringResource(id = R.string.success),
                            modifier = Modifier.weight(1f),
                            fontSize = 22.sp,
                            fontFamily = FontFamily(Font(R.font.bold2)),
                            color = colorResource(id = R.color.mainColor)
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.failed),
                            modifier = Modifier.weight(1f),
                            fontSize = 22.sp,
                            fontFamily = FontFamily(Font(R.font.bold2)),
                            color = Color.Red
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
                    space(h = 5)
                    Button(
                        onClick = {
                            shutDown.value = false
                            message.value = listOf()
                            if (isSuccess.value)
                                navController.navigate(ScreensRoute.OTPCodeScreen.route + "/$email")
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.mainColor)
                        ), modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.ok),
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun checkPasswordResponse(
    shutDown: MutableState<Boolean>,
    message: MutableState<List<String>>,
    isSuccess: MutableState<Boolean>,
    navController: NavHostController,
    shutDownError:MutableState<Boolean>,
    errorMessage: MutableState<String>
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
                            text = stringResource(id = R.string.success),
                            modifier = Modifier.weight(1f),
                            fontSize = 22.sp,
                            fontFamily = FontFamily(Font(R.font.bold2)),
                            color = colorResource(id = R.color.mainColor)
                        )
                    } else {
                        Text(
                            text = stringResource(id = R.string.failed),
                            modifier = Modifier.weight(1f),
                            fontSize = 22.sp,
                            fontFamily = FontFamily(Font(R.font.bold2)),
                            color = Color.Red
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
                                logout(
                                    shutDownError = shutDownError,
                                    errorMessage= errorMessage,
                                    navController= navController
                                )
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.mainColor)
                        ), modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.ok),
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun checkOtpCodeResponse(
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
                            text = stringResource(id = R.string.success),
                            modifier = Modifier.weight(1f),
                            fontSize = 22.sp,
                            fontFamily = FontFamily(Font(R.font.bold2)),
                            color = colorResource(id = R.color.mainColor)
                        )
                    } else {
                        Text(
                            text = stringResource(id = R.string.failed),
                            modifier = Modifier.weight(1f),
                            fontSize = 22.sp,
                            fontFamily = FontFamily(Font(R.font.bold2)),
                            color = Color.Red
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
                            text = stringResource(id = R.string.ok),
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun rejectedTrainee(
    shutDown:MutableState<Boolean>,
    message:MutableState<String>,
    barcodeValue: MutableState<String>,
    showProgress:MutableState<Boolean>
){
    if (shutDown.value) {
        Dialog(
            onDismissRequest = {
                shutDown.value = false
                message.value = ""
                barcodeValue.value = ""
                showProgress.value=false
            }
        ) {

            Card(
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .width((IntrinsicSize.Max)),
                shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp),
                elevation = 10.dp
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(15.dp)
                ) {
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Failed",
                            modifier = Modifier.padding(5.dp),
                            fontSize = 23.sp,
                            fontFamily = FontFamily(Font(R.font.bold2)),
                        )
                        Icon(
                            imageVector = Icons.Default.Error,
                            tint = Color.Red,
                            contentDescription = null,
                        )

                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = message.value,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    }
}
@Composable
fun errorDialog(
    shutDown: MutableState<Boolean>,
    errorMessage: MutableState<String>,
) {

    if (shutDown.value) {
        Dialog(
            onDismissRequest = {
                shutDown.value = false
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
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = stringResource(R.string.error),
                        fontSize = 25.sp,
                        fontFamily = FontFamily(Font(R.font.bold2)),

                        )
                    space(h = 10)
                    Text(
                        text = errorMessage.value
                    )
                    space(10)
                    Button(
                        onClick = {
                            shutDown.value = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Red
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.ok),
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.bold2))
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun scannerErrorDialog(
    shutDown: MutableState<Boolean>,
    errorMessage: MutableState<String>,
    showProgress:MutableState<Boolean>,
    barcodeValue: MutableState<String>
) {

    if (shutDown.value) {
        Dialog(
            onDismissRequest = {
                shutDown.value = false
                showProgress.value=false
                barcodeValue.value=""
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
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = stringResource(R.string.error),
                        fontSize = 25.sp,
                        fontFamily = FontFamily(Font(R.font.bold2)),

                        )
                    space(h = 10)
                    Text(
                        text = errorMessage.value
                    )
                    space(10)
                    Button(
                        onClick = {
                            shutDown.value = false
                            showProgress.value=false
                            barcodeValue.value=""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Red
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.ok),
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.bold2))
                        )
                    }
                }
            }
        }
    }
}
