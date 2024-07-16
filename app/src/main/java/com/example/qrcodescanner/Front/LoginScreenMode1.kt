package com.example.qrcodescanner.Front

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.qrcodescanner.R

@Composable
fun login1(){

    val userName = rememberSaveable() { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    var emptyPassword = rememberSaveable() { mutableStateOf(false)}
    var emaptyUserName = rememberSaveable() { mutableStateOf(false)}
    val modifierForEmptyField = Modifier
        .fillMaxSize()
        .padding(start = 10.dp, end = 10.dp)
        .border(2.dp, Color.Red, RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp))
    val modifierForNotEmptyField = Modifier
        .fillMaxSize()
        .padding(start = 10.dp, end = 10.dp)


    Box(
        modifier= Modifier
            .fillMaxSize()
    ){
       Column(
           modifier= Modifier
               .fillMaxSize()
       ){
           Box(
               modifier= Modifier
                   .weight(1.2f)
                   .fillMaxSize()
           ){
               HalfCircle(colorResource(id = R.color.mainColor))

           }
           Column(

               modifier= Modifier
                   .fillMaxSize()
                   .weight(0.8f)
                   .padding(15.dp)
           ){
               Box(
                   modifier=Modifier.weight(1f).padding(top=10.dp,bottom=10.dp)
               ){
                   if(emaptyUserName.value==false||userName.value.isNotEmpty())
                       userNameField(userName,modifierForNotEmptyField)
                   else
                       userNameField(userName,modifierForEmptyField)
               }
               Box(
                   modifier=Modifier.weight(1f).padding(top=10.dp,bottom=10.dp)
               ){
                   if(emptyPassword.value==false||password.value.isNotEmpty())
                       passwordField(password,modifierForNotEmptyField)
                   else
                       passwordField(password,modifierForEmptyField)
               }
               Box(
                   modifier=Modifier.weight(1f)
               ){

                   Button(
                       onClick = {

                       },
                       modifier=Modifier.padding(top=10.dp,start=10.dp,end=10.dp).fillMaxWidth(),
                       shape=RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                       colors=ButtonDefaults.buttonColors(
                           backgroundColor = colorResource(id = R.color.mainColor)
                       )
                   ){
                     Text(text="LOGIN",color=Color.White,modifier=Modifier.padding(5.dp))
                   }
               }

           }
       }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(top =90.dp,start=30.dp,end=30.dp)
        ){
            Image(
                painterResource(R.drawable.icpc_family),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(CircleShape),
                contentDescription = "",
            )
        }

    }
}
@Composable
fun HalfCircle(color:Color,startAngle:Int=-90,sweepAngle:Int=180) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawHalfCircle(this,color,startAngle,sweepAngle)
    }
}

fun drawHalfCircle(drawScope: DrawScope,color:Color,startAngle:Int=-90,sweepAngle:Int=180) {
    val canvasWidth = drawScope.size.width
    val canvasHeight = drawScope.size.height

    val diameter = Math.min(canvasWidth, canvasHeight)
    val radius = diameter / 2

    drawScope.drawArc(
        color = color,
        startAngle = startAngle.toFloat(),
        sweepAngle = sweepAngle.toFloat(),
        useCenter = false,
        topLeft = Offset(-(canvasWidth)/2f, 0f),
        size = Size(diameter, diameter)
    )
}
@Composable
fun passwordField(password: MutableState<String>,modifier:Modifier) {

    var passwordVisible = rememberSaveable { mutableStateOf(false) }
    Card(
        modifier=modifier,
        shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
        elevation = 3.dp,
    ) {
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            placeholder = {Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier.fillMaxSize()){
                Text(text = "Password")
            } },
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
fun userNameField(userName: MutableState<String>, modifier: Modifier) {

    Card(
        modifier =modifier,
        shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
        elevation = 3.dp,
    ) {
        TextField(
            modifier=Modifier,
            value = userName.value,
            onValueChange = { userName.value = it },
            placeholder = {Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier.fillMaxSize()){
                Text(text = "User Name")
            } },
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

//Card(
//modifier = Modifier
//.width(300.dp)
//.height(450.dp)
//,shape = RoundedCornerShape(10.dp,10.dp,10.dp,10.dp),
//elevation = 10.dp
//) {
//    Column(){
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .weight(1f)
//        ){
//            Row(){
//                Box( modifier = Modifier
//                    .fillMaxSize()
//                    .weight(1f)){
//                    Card(
//                        modifier = Modifier.fillMaxSize()
//                        , shape = RoundedCornerShape(0.dp,0.dp,1000.dp,0.dp),
//                        elevation = 10.dp,
//                        backgroundColor = colorResource(id = R.color.mainColor)
//                    ){}
//                }
//                Box( modifier = Modifier
//                    .fillMaxSize()
//                    .weight(1f)){}
//            }
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(100.dp),
//                contentAlignment = Alignment.Center
//            ){
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    elevation = 10.dp,
//                    backgroundColor = colorResource(id = R.color.mainColor)
//                ){
//                    Box(
//                        modifier = Modifier.fillMaxWidth(),
//                        contentAlignment = Alignment.Center
//                    ){
//                        Text(
//                            text="All Camps ",
//                            modifier = Modifier.padding(15.dp),
//                            color=Color.White,
//                            fontFamily = FontFamily(Font(R.font.bold2)),
//                            fontSize = 20.sp
//                        )
//                    }
//                }
//            }
//
//        }
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .weight(2f)
//        ){
//
//        }