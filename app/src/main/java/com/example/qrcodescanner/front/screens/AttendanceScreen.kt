package com.example.qrcodescanner.front.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.qrcodescanner.R
import com.example.qrcodescanner.back.API.getPresentTrainees
import com.example.qrcodescanner.back.Model.ItemData
import com.example.qrcodescanner.back.function.getMode
import com.example.qrcodescanner.front.components.AttendanceTopBae
import com.example.qrcodescanner.front.components.errorDialog
import com.example.qrcodescanner.front.components.progressBar
import com.example.qrcodescanner.front.components.space


@Composable
fun AttendanceScreen(navController: NavHostController) {


    val showProgress = remember { mutableStateOf(false) }
    val presentTraineeList = remember { mutableStateListOf<ItemData>() }
    val itemsCase = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }
    val shutDownError = remember { mutableStateOf(false) }
    val selectedTrainee = remember { mutableStateOf("") }

    LaunchedEffect(selectedTrainee.value)
    {

        getPresentTrainees(
            trainees = presentTraineeList,
            itemsCase = itemsCase,
            showProgress = showProgress,
            shutDownError = shutDownError,
            errorMessage = errorMessage,
            selectedTrainee = selectedTrainee
        )
    }

    showProgress.value = (presentTraineeList.isEmpty())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AttendanceTopBae(
            navController = navController,
            selectedTrainee = selectedTrainee
        )
        Column(
            modifier = Modifier
                .padding(top = 5.dp)
                .background(MaterialTheme.colors.surface)

        ) {
            errorDialog(
                shutDown = shutDownError,
                errorMessage = errorMessage
            )
            if (itemsCase.value == stringResource(R.string.no_trainee_has_presented_yet) ||
                itemsCase.value == stringResource(R.string.no_current_session_for_now)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = itemsCase.value,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            } else {
                progressBar(show = showProgress)
                LazyColumn(
                    modifier = Modifier.background(MaterialTheme.colors.surface)
                ) {

                    items(presentTraineeList) { trainee ->

                        traineeModel(
                            traineeName = trainee.name,
                            traineeHandle = trainee.codeForceHandle
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun traineeModel(traineeName: String, traineeHandle: String) {

    val cf_color = remember { mutableStateOf(R.color.cf_color_inL) }
    if (getMode() == stringResource(id = R.string.light_mode))
        cf_color.value = R.color.cf_color_inN
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp, bottom = 5.dp)
            .height(90.dp),
        elevation = 20.dp
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,

            ) {

            Icon(
                painter = painterResource(R.drawable.person_icon),
                tint = colorResource(id = R.color.mainColor),
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
            )

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(5f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = traineeName,
                        fontSize = 18.sp
                    )
                }
                space(h = 5)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.cf_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                    )
                    Text(
                        text = traineeHandle,
                        color = colorResource(id = cf_color.value),
                        fontSize = 15.sp,
                        modifier = Modifier.padding(start = 2.dp)

                    )
                }
            }
        }
    }
}

@Composable
fun searchAboutTrainee(
    selectedTrainee: MutableState<String>
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp),
        shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
        elevation = 3.dp,
        backgroundColor = MaterialTheme.colors.secondary,
    ) {

        TextField(
            modifier = Modifier,
            value = selectedTrainee.value,
            onValueChange = { selectedTrainee.value = it },
            placeholder = {
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.search))
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
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                )
            }
        )

    }
}