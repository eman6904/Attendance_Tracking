package com.example.qrcodescanner.Front

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.qrcodescanner.R
import com.example.qrcodescanner.ScreensRoute
import com.example.qrcodescanner.Back.DataClasses.LoginRequirements
import com.example.qrcodescanner.Back.DataClasses.UserData
import com.example.qrcodescanner.Back.functions.*
import com.example.qrcodescanner.MainActivity
import kotlinx.coroutines.launch


@Composable
fun login2(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
       val  errorMessage= remember { mutableStateOf("")}
       val  shutDownError= remember { mutableStateOf(false)}
        errorDialog(shutDownError,errorMessage)
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
            mainContent2(
                navController,
                shutDownError,
                errorMessage
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
                if(getMode().toString()=="Dark Mode"||getMode()==null){
                    Image(
                        painterResource(R.drawable.icpc_logo_light),
                        modifier = Modifier
                            .size(80.dp)
                            .aspectRatio(1f),
                        contentDescription = "",
                    )

                }else{
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
                text="ISc System",
                fontSize = 15.sp,
                color=Color.White,
            )
            Text(
                "Attendance registration",
                color=Color.White,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.bold2)),
            )
        }
    }
}

@Composable
fun mainContent2(
    navController: NavHostController,
    shutDownError:MutableState<Boolean>,
    errorMessage:MutableState<String>
) {

    val userName = rememberSaveable() { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    var emptyPassword = rememberSaveable() { mutableStateOf(false) }
    var emptyUserName = rememberSaveable() { mutableStateOf(false) }
    val scop = rememberCoroutineScope()
    val rememberMe= MainActivity.rememberMe_sharedPref.getString(MainActivity.REMEMBER_ME,null)
    val userData = remember { mutableStateOf(UserData("", "",
        "", emptyList(), "", "")) }
    val modifierForEmptyField = Modifier
        .fillMaxWidth()
        .padding(start = 20.dp, end = 20.dp)
        .border(2.dp, Color.Red, RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp))
    val modifierForNotEmptyField = Modifier
        .fillMaxWidth()
        .padding(start = 20.dp, end = 20.dp)

    val showProgress = remember { mutableStateOf(false) }

    if(rememberMe.toString()=="true"&& getLoginData() !=null){

        userName.value= getLoginData()!!.userName
        password.value= getLoginData()!!.password
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(start = 5.dp, end = 5.dp, top = 40.dp),
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
                                  loginData =  LoginRequirements(
                                       userName = userName.value,
                                       password = password.value
                                   ),
                                   userData = userData,
                                   navController = navController,
                                   shutDownError = shutDownError,
                                   errorMessage = errorMessage,
                                   showProgress = showProgress
                               )
                           }
                           showProgress.value = true
                       }
                       emptyPassword.value = password.value.isEmpty()
                       emptyUserName.value = userName.value.isEmpty()

                   },
                   modifier = Modifier
                       .padding(start = 20.dp, end = 20.dp, top = 5.dp)
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
                      style = TextStyle(
                          color = MaterialTheme.colors.onSurface),
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
        backgroundColor =MaterialTheme.colors.secondary,
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
                backgroundColor =MaterialTheme.colors.secondary,
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
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colors.onSurface
            ),
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
        backgroundColor =MaterialTheme.colors.secondary,
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
                backgroundColor =MaterialTheme.colors.secondary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Gray,
                disabledIndicatorColor = Color.Transparent,
                disabledTextColor = Color.Transparent

            ),
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colors.onSurface
            ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
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

    val oldState= MainActivity.rememberMe_sharedPref.getString(MainActivity.REMEMBER_ME,null)
    var myState = remember { mutableStateOf(false) }
    if(oldState=="false"||oldState==null)
       myState.value=false
    else
        myState.value=true

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            modifier = Modifier.clip(CircleShape),
            checked = myState.value,
            onCheckedChange = {
                myState.value = it
                MainActivity.rememberMe_sharedPref.edit().putString(MainActivity.REMEMBER_ME, it.toString()).apply()
                              },
            colors = CheckboxDefaults.colors(
                uncheckedColor = colorResource(id = R.color.mainColor),
                checkedColor = colorResource(id = R.color.mainColor),
                checkmarkColor = Color.White,
            )
        )
        Text(text = item)
    }
}

