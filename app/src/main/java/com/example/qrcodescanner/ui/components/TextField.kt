package com.example.qrcodescanner.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.qrcodescanner.MainActivity.Companion.viewModelHelper
import com.example.qrcodescanner.R

@Composable
fun UserNameTextField(
   placeHolder:String,
   isError:MutableState<Boolean>,
   errorValue:MutableState<String>,
   leadingIcon: @Composable (() -> Unit)? = null,
   keyboardOptions: KeyboardOptions,
   modifier: Modifier,
){
    Column() {
        Card(
            modifier =modifier,
            shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
            elevation = 3.dp,
            backgroundColor = MaterialTheme.colors.secondary,
        ) {
            val userName = viewModelHelper.userName.collectAsState()
            TextField(
                value = userName.value,
                onValueChange = { viewModelHelper.setUserName(it) },
                placeholder = {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(text =placeHolder)
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
                keyboardOptions =keyboardOptions,
                leadingIcon =leadingIcon,
                isError = isError.value,
                textStyle = LocalTextStyle.current.copy(
                    color = MaterialTheme.colors.onSurface
                ),
                trailingIcon = {

                    if(isError.value){
                        Icon(imageVector = Icons.Default.ErrorOutline, contentDescription = null)
                    }
                }
            )
        }
        if(errorValue.value.isNotEmpty()){
            errorValue?.let {
                Text(
                    text = errorValue.value,
                    color = Color.Red,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .padding(top = 3.dp, start = 20.dp, end = 20.dp)
                        .fillMaxWidth()

                )
            }
        }
    }
}
@Composable
fun EmailTextField(
    placeHolder:String,
    isError:MutableState<Boolean>,
    errorValue:MutableState<String>,
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier,
){
    Column() {
        Card(
            modifier =modifier,
            shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
            elevation = 3.dp,
            backgroundColor = MaterialTheme.colors.secondary,
        ) {
            val email = viewModelHelper.email.collectAsState()
            TextField(
                value = email.value,
                onValueChange = { viewModelHelper.setEmail(it) },
                placeholder = {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(text =placeHolder)
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
                keyboardOptions =keyboardOptions,
                leadingIcon =leadingIcon,
                isError = isError.value,
                textStyle = LocalTextStyle.current.copy(
                    color = MaterialTheme.colors.onSurface
                ),
                trailingIcon = {

                    if(isError.value){
                        Icon(imageVector = Icons.Default.ErrorOutline, contentDescription = null)
                    }
                }
            )
        }
        if(errorValue.value.isNotEmpty()){
            errorValue?.let {
                Text(
                    text = errorValue.value,
                    color = Color.Red,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .padding(top = 3.dp, start = 20.dp, end = 20.dp)
                        .fillMaxWidth()

                )
            }
        }
    }
}

@Composable
fun PasswordTextField(
    placeHolder:String,
    isError:MutableState<Boolean>,
    errorValue:MutableState<String>,
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier,
){

    var passwordVisible = rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        passwordVisible.value=(placeHolder!="Password")
    }

    Column() {
        Card(
            modifier =modifier,
            shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
            elevation = 3.dp,
            backgroundColor = MaterialTheme.colors.secondary,
        ) {
            val password = viewModelHelper.password.collectAsState()
            TextField(
                value = password.value,
                onValueChange = { viewModelHelper.setPassword(it) },
                placeholder = {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(text =placeHolder)
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
                keyboardOptions =keyboardOptions,
                visualTransformation =
                if (passwordVisible.value) VisualTransformation.None
                else PasswordVisualTransformation(),
                leadingIcon =leadingIcon,
                isError = isError.value,
                textStyle = LocalTextStyle.current.copy(
                    color = MaterialTheme.colors.onSurface
                ),
                trailingIcon = {

                    if(isError.value){

                        Icon(imageVector = Icons.Default.ErrorOutline, contentDescription = null)

                    }else{
                        val image = if (passwordVisible.value)
                            Icons.Filled.Visibility
                        else
                            Icons.Filled.VisibilityOff

                        val description =
                            if (passwordVisible.value) "Hide password" else "Show password"

                        IconButton(onClick = { passwordVisible.value = !(passwordVisible.value) }) {
                            Icon(imageVector = image, description)
                        }
                    }
                }
            )
        }
        if(errorValue.value.isNotEmpty()){
            errorValue?.let {
                Text(
                    text = errorValue.value,
                    color = Color.Red,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .padding(top = 3.dp, start = 20.dp, end = 20.dp)
                        .fillMaxWidth()

                )
            }
        }
    }
}
@Composable
fun RestPasswordTextField(
    placeHolder:String,
    isError:MutableState<Boolean>,
    errorValue:MutableState<String>,
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier,
){

    var passwordVisible = rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        passwordVisible.value=(placeHolder!="Password")
    }

    Column() {
        Card(
            modifier =modifier,
            shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
            elevation = 3.dp,
            backgroundColor = MaterialTheme.colors.secondary,
        ) {
            val password = viewModelHelper.newPassword.collectAsState()
            TextField(
                value = password.value,
                onValueChange = { viewModelHelper.setNewPassword(it) },
                placeholder = {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(text =placeHolder)
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
                keyboardOptions =keyboardOptions,
                visualTransformation =
                if (passwordVisible.value) VisualTransformation.None
                else PasswordVisualTransformation(),
                leadingIcon =leadingIcon,
                isError = isError.value,
                textStyle = LocalTextStyle.current.copy(
                    color = MaterialTheme.colors.onSurface
                ),
                trailingIcon = {

                    if(isError.value){

                        Icon(imageVector = Icons.Default.ErrorOutline, contentDescription = null)

                    }else{
                        val image = if (passwordVisible.value)
                            Icons.Filled.Visibility
                        else
                            Icons.Filled.VisibilityOff

                        val description =
                            if (passwordVisible.value) "Hide password" else "Show password"

                        IconButton(onClick = { passwordVisible.value = !(passwordVisible.value) }) {
                            Icon(imageVector = image, description)
                        }
                    }
                }
            )
        }
        if(errorValue.value.isNotEmpty()){
            errorValue?.let {
                Text(
                    text = errorValue.value,
                    color = Color.Red,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .padding(top = 3.dp, start = 20.dp, end = 20.dp)
                        .fillMaxWidth()

                )
            }
        }
    }
}
@Composable
fun ConfirmPasswordTextField(
    placeHolder:String,
    isError:MutableState<Boolean>,
    errorValue:MutableState<String>,
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier,
){

    Column() {
        Card(
            modifier =modifier,
            shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
            elevation = 3.dp,
            backgroundColor = MaterialTheme.colors.secondary,
        ) {
            val passConfirm = viewModelHelper.passwordConfirm.collectAsState()
            TextField(
                value = passConfirm.value,
                onValueChange = { viewModelHelper.setPassConfirm(it) },
                placeholder = {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(text =placeHolder)
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
                keyboardOptions =keyboardOptions,
                leadingIcon =leadingIcon,
                isError = isError.value,
                textStyle = LocalTextStyle.current.copy(
                    color = MaterialTheme.colors.onSurface
                ),
                trailingIcon = {
                    if(isError.value){
                        Icon(imageVector = Icons.Default.ErrorOutline, contentDescription = null)
                    }
                }
            )
        }
        if(errorValue.value.isNotEmpty()){
            errorValue?.let {
                Text(
                    text = errorValue.value,
                    color = Color.Red,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .padding(top = 3.dp, start = 20.dp, end = 20.dp)
                        .fillMaxWidth()

                )
            }
        }
    }
}
@Composable
fun pointsTextField() {

    val points= viewModelHelper.pointsString.collectAsState()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp),
        shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 3.dp,
    ) {
        TextField(
            modifier = Modifier,
            value = points.value,
            onValueChange = {
                if(points.value.isEmpty()||it.isEmpty()){

                    viewModelHelper.setPointsString(it)
                }
                else if(it.isDigitsOnly()&&points.value.isDigitsOnly()){
                    // Log.d("points",(points.value.toLong()).toString())
                    if(
                        points.value.toLong()<92233720368547758||
                        it.toLong()<points.value.toLong()) {
                        viewModelHelper.setPointsString(it)
                    }
                }else if(it.isDigitsOnly()&&!points.value.isDigitsOnly()){

                    viewModelHelper.setPointsString(it)

                }else if(points.value.isDigitsOnly()&&!it.isDigitsOnly()){

                    viewModelHelper.setPointsString(it)
                }
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.points),
                    color = MaterialTheme.colors.onSurface,
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = colorResource(id = R.color.mainColor),
                disabledIndicatorColor = Color.Transparent,
                disabledTextColor = Color.Transparent

            ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )
    }
}
