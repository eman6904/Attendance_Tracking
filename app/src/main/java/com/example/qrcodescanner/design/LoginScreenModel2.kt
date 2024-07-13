package com.example.qrcodescanner.design

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.example.qrcodescanner.R
import com.example.qrcodescanner.ScreensRoute
import com.example.qrcodescanner.coding.DataClasses.LoginData
import com.example.qrcodescanner.coding.DataClasses.UserData
import com.example.qrcodescanner.coding.functions.login
import kotlinx.coroutines.launch
import okhttp3.internal.notify

@Composable
fun login2(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(colorResource(id = R.color.mainColor)),
            contentAlignment = Alignment.BottomCenter
        ) {
            headerSection()
        }
        Card(
            shape = RoundedCornerShape(50.dp, 50.dp, 0.dp, 0.dp),
            modifier = Modifier
                .weight(1.5f)
                .background(colorResource(id = R.color.mainColor))
        ) {
            mainContent2(navController)
        }
    }
}

@Composable
fun headerSection() {
    Column(

        modifier = Modifier
            .padding(start = 20.dp)
            .fillMaxSize()
    ) {
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
                Image(
                    painterResource(R.drawable.icpc_yellow_logo),
                    modifier = Modifier
                        .size(80.dp)
                        .aspectRatio(1f),
                    contentDescription = "",
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text("ISc System", color = Color.White, fontSize = 15.sp)
            Text(
                "Attendance registration", color = Color.White,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.bold2)),
            )
            //   Text("ICPC sohag Community Trainings",color=Color.White, fontSize = 15.sp)
        }
    }
}

@Composable
fun mainContent2(navController: NavHostController) {

    val userName = rememberSaveable() { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    var emptyPassword = rememberSaveable() { mutableStateOf(false) }
    var emptyUserName = rememberSaveable() { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val scop = rememberCoroutineScope()
    val userData = remember { mutableStateOf(UserData("", "",
        "", emptyList(), "", "")) }
    val modifierForEmptyField = Modifier
        .fillMaxWidth()
        .padding(start = 20.dp, end = 20.dp)
        .border(2.dp, Color.Red, RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp))
    val modifierForNotEmptyField = Modifier
        .fillMaxWidth()
        .padding(start = 20.dp, end = 20.dp)

    val message = remember { mutableStateOf("") }
    val showProgress = remember { mutableStateOf(false) }

    if (message.value.isNotEmpty()) {

        showProgress.value = false
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(start = 5.dp, end = 5.dp,top=40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

       Box(
           modifier= Modifier
               .weight(1f)
               .fillMaxSize(),
           contentAlignment = Alignment.Center
       ){
           if (emptyUserName.value == false || userName.value.isNotEmpty())
               userNameField2(userName, modifierForNotEmptyField)
           else
               userNameField2(userName, modifierForEmptyField)
       }
      //  space(h = 25)

       Box(
           modifier= Modifier
               .weight(1f)
               .fillMaxSize(),
           contentAlignment = Alignment.Center
       ){
           if (emptyPassword.value == false || password.value.isNotEmpty())
               passwordField2(password, modifierForNotEmptyField)
           else
               passwordField2(password, modifierForEmptyField)
       }
       // space(h = 25)
       Box(
           modifier= Modifier
               .weight(1f)
               .fillMaxSize(),
           contentAlignment = Alignment.Center
       ){
           Column() {

               Button(
                   onClick = {
                       if (userName.value.isNotEmpty() && password.value.isNotEmpty()) {
                           scop.launch {
                               login(
                                   LoginData(
                                       userName = userName.value,
                                       password = password.value,
                                       true
                                   ),
                                   message,
                                   userData,
                                   lifecycleOwner,
                                   navController
                               )
                           }
                           showProgress.value = true
                       }
                       emptyPassword.value = password.value.isEmpty()
                       emptyUserName.value = userName.value.isEmpty()

                   },
                   modifier = Modifier
                       .padding(start = 20.dp, end = 20.dp,top=5.dp)
                       .fillMaxWidth(),
                   shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                   colors = ButtonDefaults.buttonColors(
                       backgroundColor = colorResource(id = R.color.mainColor)
                   )
               ) {
                   Text(text = "LOGIN", color = Color.White, modifier = Modifier.padding(9.dp))
               }
               space(5)
               Box(
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(start = 10.dp, end = 20.dp, top = 10.dp),
                   contentAlignment = Alignment.TopStart
               ) {
                   checkBox("Remember me")
               }
           }
       }
       Box(
           modifier= Modifier
               .weight(1f)
               .fillMaxSize(),
           contentAlignment = Alignment.Center
       ){
          Column(){
              if (showProgress.value) {

                  Column(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalAlignment = Alignment.CenterHorizontally,
                      verticalArrangement = Arrangement.Center
                  ) {
                      CircularProgressIndicator(
                          color = colorResource(id = R.color.mainColor),
                          strokeWidth = 3.dp,
                          modifier = Modifier.size(25.dp)
                      )
                  }
              }
              Box(
                  modifier = Modifier
                      .fillMaxWidth()
                      .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                  contentAlignment = Alignment.Center
              ) {
                  ClickableText(
                      text = AnnotatedString("Forgot Your Password?"),
                      onClick =
                      {
                          navController.navigate(ScreensRoute.ForgetPasswordScreen.route)
                      }
                  )
              }
          }
       }
    }
}

@Composable
fun passwordField2(password: MutableState<String>, modifier: Modifier) {

    var passwordVisible = rememberSaveable { mutableStateOf(false) }
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
        elevation = 3.dp,
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
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
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
                    tint = Color.DarkGray
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
fun userNameField2(userName: MutableState<String>, modifier: Modifier) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
        elevation = 3.dp,
    ) {

        TextField(
            modifier = Modifier,
            value = userName.value,
            onValueChange = { userName.value = it },
            placeholder = {
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "User Name")
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
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.DarkGray
                )
            }
        )

    }
}

@Composable
fun progressBar(show: MutableState<Boolean>) {

    if (show.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = colorResource(id = R.color.mainColor), strokeWidth = 3.dp,
                modifier = Modifier.size(30.dp)
            )
        }
    }

}

@Composable
fun checkBox(item: String) {
    var myState = remember { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            modifier = Modifier.clip(CircleShape),
            checked = myState.value,
            onCheckedChange = { myState.value = it },
            colors = CheckboxDefaults.colors(
                uncheckedColor = colorResource(id = R.color.mainColor),
                checkedColor = colorResource(id = R.color.mainColor),
                checkmarkColor = Color.White,
            )
        )
        Text(text = item)
    }
}

