package com.example.qrcodescanner.Front

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.qrcodescanner.R
import com.example.qrcodescanner.Back.DataClasses.ExtraPointRequirements
import com.example.qrcodescanner.Back.DataClasses.ItemData
import com.example.qrcodescanner.Back.functions.errorDialog
import com.example.qrcodescanner.Back.functions.getAllTrainees
import com.example.qrcodescanner.Back.functions.traineePointsUpdate


@Composable
fun extraPoints(navHostController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.mainColor)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        var pointType = listOf("Plus", "Minus")
        var selectedAction by remember { mutableStateOf("Points Action") }
        var points = remember { mutableStateOf("") }
        var points2 = remember { mutableStateOf(0) }
        var searchedTrainee = remember { mutableStateOf("") }
        var traineeId = remember { mutableStateOf("") }
        val traineeNameRequired = remember { mutableStateOf(false) }
        val pointsRequired = remember { mutableStateOf(false) }
        val pointActionRequired = remember { mutableStateOf(false) }
        val showSending = remember { mutableStateOf(false) }
        val errorMessage = remember { mutableStateOf("") }
        val shutDownError = remember { mutableStateOf(false) }

        errorDialog(
            shutDown = shutDownError,
            errorMessage = errorMessage
        )

        if (points.value != "")
            points2.value = points.value.toInt()
        Text(
            text = "Extra Points",
            fontSize = 30.sp,
            fontFamily = FontFamily(Font(R.font.bold2)),
            color = Color.White,
            modifier = Modifier.padding(bottom = 30.dp)
        )
        space(h = 30)
        search(searchedTrainee, traineeId)
        requiredField(show = traineeNameRequired)
        space(h = 50)
        pointsField(points = points)
        requiredField(show = pointsRequired)
        space(h = 50)
        selectPointAction(
            itemList = pointType,
            selectedItem = selectedAction,
            onItemSelected = { selectedAction = it }
        )
        requiredField(show = pointActionRequired)
        space(h = 25)
        updatePointsButton(
            traineeNameRequired,
            pointsRequired,
            pointActionRequired,
            traineeId,
            points2,
            selectedAction,
            onItemSelected = { selectedAction = it },
            points,
            searchedTrainee,
            showSending,
            shutDownError,
            errorMessage
        )
        if (showSending.value) {
            Text(
                text = "Sending....",
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun search(searchedTrainee: MutableState<String>, traineeId: MutableState<String>) {

    var trainees = remember { mutableListOf<ItemData>() }
    val errorMessage = remember { mutableStateOf("") }
    val shutDownError = remember { mutableStateOf(false) }
    val showProgress = remember { mutableStateOf(false) }
    val notrainees = remember { mutableStateOf(false) }
    var active = remember { mutableStateOf(false) }

    if (trainees.isEmpty() && notrainees.value == false)
        showProgress.value = true
    else
        showProgress.value = false

    LaunchedEffect(Unit)
    {
        getAllTrainees(
            trainees = trainees,
            shutDownError = shutDownError,
            errorMessage = errorMessage,
            showProgress = showProgress,
            noTrainee = notrainees
        )
    }
    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp),
        shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colors.surface,
            inputFieldColors = androidx.compose.material3.TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colors.onSurface,
                unfocusedTextColor = MaterialTheme.colors.onSurface
            )
        ),
        query = searchedTrainee.value,
        onQueryChange = {
            searchedTrainee.value = it
        },
        onSearch = {
            active.value = false
        },
        active = active.value,
        onActiveChange = {
            active.value = it
        },
        placeholder = {
            Text(
                text = "Search Name",
                color = MaterialTheme.colors.onSurface
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search icon",
                tint = MaterialTheme.colors.onSurface
            )
        },
        trailingIcon = {

            if (active.value) {
                Icon(
                    modifier = Modifier.clickable {
                        if (searchedTrainee.value.isNotEmpty()) {
                            searchedTrainee.value = ""
                            active.value = false
                        } else
                            active.value = false
                    },
                    imageVector = Icons.Default.Close,
                    tint = MaterialTheme.colors.onSurface,
                    contentDescription = "Close icon"
                )
            }
        }
    ) {
        progressBar(show = showProgress)
        errorDialog(shutDownError, errorMessage)
        if (notrainees.value) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No trainees in this camp"
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize(),
            ) {
                items(items = trainees.filter {
                    it.name.contains(searchedTrainee.value)
                }) {
                    ColumnItem(
                        active = active,
                        trainee = it,
                        searchedText = searchedTrainee,
                        traineeId
                    )
                }
            }
        }
    }
}

@Composable
fun selectPointAction(
    itemList: List<String>,
    selectedItem: String,
    onItemSelected: (selectedItem: String) -> Unit,
    // through that we can change value of selectedItem,

) {
    var expanded by rememberSaveable() { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp)
            .clip(RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp))
    ) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.background(Color.White)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (selectedItem == "Plus") {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.Green
                    )
                } else if (selectedItem == "Minus") {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = null,
                        tint = Color.Red
                    )
                }
                Text(
                    text = selectedItem,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .weight(8f),
                    fontFamily = FontFamily.Default,
                    fontSize = 15.sp
                )
                Icon(
                    modifier = Modifier.weight(1f),
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                )
            }
        }
    }
    Box() {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            //to control dropDownMenu position
            // offset = DpOffset(x = (-1).dp, y = (-250).dp)
        ) {
            itemList.forEach {
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onItemSelected(it)
                    }
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (it == "Plus") {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = Color.Green
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = null,
                                tint = Color.Red
                            )
                        }
                        Text(text = it, fontSize = 20.sp)
                    }
                }
            }
        }

    }
}

@Composable
fun pointsField(points: MutableState<String>) {

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
            onValueChange = { points.value = it },
            placeholder = {
                Text(
                    text = "Points",
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

@Composable
fun updatePointsButton(
    traineeNameRequire: MutableState<Boolean>,
    pointsRequired: MutableState<Boolean>,
    pointActionRequired: MutableState<Boolean>,
    traineeId: MutableState<String>,
    points: MutableState<Int>,
    pointsAction: String,
    onItemSelected: (pointsAction: String) -> Unit,
    pointss: MutableState<String>,
    searchedTrainee: MutableState<String>,
    showSending: MutableState<Boolean>,
    shutDownError: MutableState<Boolean>,
    errorMessage: MutableState<String>
) {

    if (pointsAction == "Minus" && points.value > 0)
        points.value *= -1

    val showMessage = remember { mutableStateOf(false) }
    val isSuccess = remember { mutableStateOf(false) }
    val message = remember { mutableStateOf("") }


    if (message.value.isNotEmpty()) {
        showMessage.value = true
        showSending.value = false

        if (isSuccess.value) {
            onItemSelected("Points Action")
            pointss.value = ""
            searchedTrainee.value = ""
        }
    }
    if (showMessage.value)
        message(showMessage = showMessage, isSuccess = isSuccess, message)

    Card(
        modifier = Modifier
            .clickable {

                if (searchedTrainee.value.isNotEmpty() && pointss.value.isNotEmpty() && pointsAction != "Points Action") {
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
                    pointActionRequired.value = (pointsAction == "Points Action")


                } else {
                    traineeNameRequire.value = (searchedTrainee.value.isEmpty())
                    pointsRequired.value = (pointss.value.isEmpty())
                    pointActionRequired.value = (pointsAction == "Points Action")
                }

            }
            .fillMaxWidth()
            .padding(15.dp),
        backgroundColor = colorResource(id = R.color.mainColor2),
        shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
        elevation = 15.dp

    ) {
        Text(
            text = "Done",
            modifier = Modifier.padding(15.dp),
            color = Color.White,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.bold2))
        )
    }
}

@Composable
fun ColumnItem(
    active: MutableState<Boolean>, trainee: ItemData,
    searchedText: MutableState<String>,
    traineeId: MutableState<String>
) {

    Text(
        text = trainee.name,
        modifier = Modifier
            .clickable {
                searchedText.value = trainee.name
                traineeId.value = trainee.id
                active.value = false
            }
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp),
        style = TextStyle(
            color = MaterialTheme.colors.onSurface
        )
    )

    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(1.dp)
    )
}

@Composable
fun space(h: Int) {
    Spacer(
        modifier = Modifier
            .height(h.dp)
            .fillMaxWidth()
    )
}

@Composable
fun message(
    showMessage: MutableState<Boolean>,
    isSuccess: MutableState<Boolean>,
    message: MutableState<String>
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
                            text = "Success!",
                            modifier = Modifier,
                            fontSize = 25.sp,
                            fontFamily = FontFamily(Font(R.font.bold2))
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Trainee points have been updated. ",
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
                        if (isSuccess.value) {
                            Button(
                                onClick = {
                                    showMessage.value = false
                                    message.value = ""
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = colorResource(id = R.color.mainColor),
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "OK",
                                    fontSize = 15.sp,
                                    color = Color.White,
                                    fontFamily = FontFamily(Font(R.font.bold2))
                                )
                            }
                        } else {
                            Button(
                                onClick = {
                                    showMessage.value = false
                                    message.value = ""
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.Red
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "OK",
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
fun requiredField(show: MutableState<Boolean>) {

    if (show.value) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Text(
                text = "*this field is required",
                color = colorResource(id = R.color.red)
            )
        }
    }
}