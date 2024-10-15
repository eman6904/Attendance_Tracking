package com.example.qrcodescanner.ui.screens.auth


import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.qrcodescanner.R
import com.example.qrcodescanner.navigation.ScreensRoute
import com.example.qrcodescanner.data.model.LoginRequirements
import com.example.qrcodescanner.data.utils.*
import com.example.qrcodescanner.ui.components.checkBox
import com.example.qrcodescanner.ui.components.space
import com.example.qrcodescanner.ui.utils.checkPassword
import com.example.qrcodescanner.ui.utils.checkUserName
import com.example.qrcodescanner.MainActivity
import com.example.qrcodescanner.MainActivity.Companion.LOGIN_REQUIREMENTS
import com.example.qrcodescanner.MainActivity.Companion.userData_sharedPref
import com.example.qrcodescanner.MainActivity.Companion.viewModelHelper
import com.example.qrcodescanner.data.apis.UIState
import com.example.qrcodescanner.data.viewModel.authViewModels.LoginViewModel
import com.example.qrcodescanner.ui.components.PasswordTextField
import com.example.qrcodescanner.ui.components.UserNameTextField
import com.example.qrcodescanner.ui.components.errorDialog
import com.google.gson.Gson
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController, viewModel: LoginViewModel = hiltViewModel()) {


    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) {
        val rememberMe =
            MainActivity.rememberMe_sharedPref.getString(MainActivity.REMEMBER_ME, null)


        if (rememberMe.toString() == "true" && getLoginData() != null) {

            viewModelHelper.setUserName(getLoginData()!!.userName)
            viewModelHelper.setPassword(getLoginData()!!.password)

        }
    }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colors.surface)
    ) {
        val (header, card) = createRefs()
        val errorMessage = remember { mutableStateOf("") }
        val shutDownError = remember { mutableStateOf(false) }
        errorDialog(shutDownError, errorMessage)

        Box(
            modifier = Modifier
                .constrainAs(header) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(card.top)
                }
                .fillMaxSize()
                .height(IntrinsicSize.Max)
                .background(colorResource(id = R.color.mainColor)),
            contentAlignment = Alignment.BottomCenter
        ) {
            headerSection()
        }

        Card(
            shape = RoundedCornerShape(50.dp, 50.dp, 0.dp, 0.dp),
            modifier = Modifier
                .constrainAs(card) {
                    top.linkTo(header.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .fillMaxWidth()
                .background(colorResource(id = R.color.mainColor))
        ) {
            mainContent(
                shutDownError,
                errorMessage,
                navController,
                viewModel
            )
        }

    }
}

@Composable
fun headerSection() {
    Column(

        modifier = Modifier
            .padding(start = 20.dp)
            .fillMaxSize()
            .background(colorResource(id = R.color.mainColor))
    ) {
        //space(30)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(2f),
            contentAlignment = Alignment.BottomStart
        ) {
            Card(
                modifier = Modifier,
                shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp),

                ) {
                if (getMode().toString() == stringResource(R.string.dark_mode) || getMode() == null) {
                    Image(
                        painterResource(R.drawable.icpc_logo_light),
                        modifier = Modifier
                            .size(80.dp)
                            .aspectRatio(1f),
                        contentDescription = "",
                    )

                } else {
                    Image(
                        painterResource(R.drawable.icpc_logo_night),
                        modifier = Modifier
                            .size(80.dp)
                            .aspectRatio(1f),
                        contentDescription = "",
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.isc_system),
                fontSize = 15.sp,
                color = Color.White,
            )
            Text(
                stringResource(R.string.attendance_registration),
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.bold2)),
            )
            space(h = 30)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun mainContent(
    shutDownError: MutableState<Boolean>,
    errorMessage: MutableState<String>,
    navController: NavHostController,
    viewModel: LoginViewModel
) {

    val password = viewModelHelper.password.collectAsState()
    val userName = viewModelHelper.userName.collectAsState()
    var isUserNameError = remember() { mutableStateOf(false) }
    val isPasswordError = remember { mutableStateOf(false) }
    val passError = remember { mutableStateOf("") }
    val userNameError = remember { mutableStateOf("") }
    val showProgress = remember { mutableStateOf(false) }
    val scop = rememberCoroutineScope()
    val loginState by viewModel.state.collectAsState()

    when (loginState) {

        is UIState.Loading -> {

            showProgress.value = true
        }

        is UIState.Error -> {

            shutDownError.value = true
            showProgress.value = false
            errorMessage.value = (loginState as UIState.Error).message
        }

        is UIState.Success -> {

            LaunchedEffect(Unit) {
                val loginResponse = (loginState as UIState.Success).data
                val result = loginResponse!!.message
                if (result == "Success") {

                    val gson=Gson()
                    val json2 = gson.toJson(loginResponse.data)
                    MainActivity.currentUser_sharedPref.edit()
                        .putString(MainActivity.CURRENT_USER, json2).apply()

                    val json3 = gson.toJson(LoginRequirements(
                        userName = userName.value,
                        password = password.value
                    ))

                    userData_sharedPref.edit().putString(LOGIN_REQUIREMENTS, json3)
                        .apply()

                    navController.navigate(ScreensRoute.MainScreen.route)

                } else {

                    shutDownError.value = true
                    errorMessage.value = result
                }
                showProgress.value = false
            }
        }
        else -> {
            Log.d("state", "else")
        }
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(start = 5.dp, end = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {

        space(40)
        UserNameTextField(
            placeHolder = stringResource(R.string.user_name),
            isError = isUserNameError,
            errorValue = userNameError,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
        )
        space(h = 25)
        PasswordTextField(
            placeHolder = stringResource(R.string.password),
            isError = isPasswordError,
            errorValue = passError,
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
                .padding(start = 20.dp, end = 20.dp),
        )
        space(h = 20)
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val keyboardController = LocalSoftwareKeyboardController.current
            val context = LocalContext.current
            Button(
                onClick = {

                    keyboardController?.hide()
                    val checkUserNameResult = checkUserName(
                        userName = userName.value,
                        isUserNameError = isUserNameError,
                        userNameError = userNameError,
                        context
                    )
                    val checkPasswordResult = checkPassword(
                        password = password.value,
                        isPassError = isPasswordError,
                        passError = passError,
                        context
                    )
                    if (!checkPasswordResult && !checkUserNameResult) {
                        scop.launch {
                            viewModel.login(
                                loginRequirements = LoginRequirements(
                                    userName = userName.value,
                                    password = password.value
                                )
                            )
                        }
                        showProgress.value = true
                    }
                },
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 5.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.mainColor)
                )
            ) {
                Text(
                    text = stringResource(R.string.login),
                    color = Color.White,
                    modifier = Modifier.padding(9.dp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 20.dp, top = 5.dp),
                contentAlignment = Alignment.TopStart
            ) {
                checkBox(stringResource(R.string.remember_me))
            }

        }
        space(10)
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (showProgress.value) {

                CircularProgressIndicator(
                    color = colorResource(id = R.color.mainColor),
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(25.dp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 20.dp, end = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                ClickableText(
                    text = AnnotatedString(stringResource(R.string.forgot_your_password)),
                    style = TextStyle(
                        color = MaterialTheme.colors.onSurface
                    ),
                    onClick =
                    {
                        navController.navigate(ScreensRoute.ForgetPasswordScreen.route)
                    }
                )
            }

        }
    }
}