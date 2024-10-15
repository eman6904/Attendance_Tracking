package com.example.qrcodescanner.ui.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.qrcodescanner.data.model.ItemData
import com.example.qrcodescanner.data.utils.getCurrentCamp
import com.example.qrcodescanner.MainActivity.Companion.SELECTED_CAMPP
import com.example.qrcodescanner.MainActivity.Companion.selectedCamp_sharedPref
import com.example.qrcodescanner.R
import com.example.qrcodescanner.data.apis.UIState
import com.example.qrcodescanner.data.viewModel.traineeViewModels.CampsViewModel
import com.example.qrcodescanner.navigation.ScreensRoute
import com.example.qrcodescanner.ui.screens.trainee.sadNews
import com.google.gson.Gson

@Composable
fun selectCamp(
    selectCampDialog: MutableState<Boolean>,
    currentCampName: MutableState<String>,
    navController:NavHostController,
    viewModel: CampsViewModel
) {


    val camps = remember { mutableStateListOf<ItemData>() }
    val showProgress = remember { mutableStateOf(false) }
    val itemsCase = remember { mutableStateOf("") }
    val gson = Gson()
    val errorMessage = remember { mutableStateOf("") }
    val shutDownError = remember { mutableStateOf(false) }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCamps()
    }
    when (state) {

        is UIState.Loading -> {

            showProgress.value = true
        }

        is UIState.Error -> {

            shutDownError.value = true
            showProgress.value = false
            errorMessage.value = (state as UIState.Error).message
        }

        is UIState.Success -> {

            LaunchedEffect(Unit) {

                camps.clear()
                itemsCase.value=""

                val response = (state as UIState.Success).data

                if (response.message=="Unauthorized.")
                    navController.navigate(ScreensRoute.LogInScreen.route)

                else if(response.data.isEmpty()) {

                    itemsCase.value = "No Camps"
                }else{

                    camps.addAll(response.data)
                }
                showProgress.value = false
                shutDownError.value = false
            }
        }
        else -> {
            Log.d("state", "else")
        }
    }

    val selectedItem = remember { mutableStateOf("") }
    if (getCurrentCamp() != null)
        selectedItem.value = getCurrentCamp()!!.name

    progressBar(show = showProgress)

    if (shutDownError.value)
        sadNews(message = errorMessage)
    else {
        if (itemsCase.value == stringResource(R.string.no_camps)) {
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

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                camps.forEach() { camp ->
                    val json2 = gson.toJson(camp)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(15.dp)
                            .fillMaxWidth()
                            .clickable {

                                selectedItem.value = camp.name
                                selectedCamp_sharedPref
                                    .edit()
                                    .putString(SELECTED_CAMPP, json2)
                                    .apply()
                                currentCampName.value = getCurrentCamp()!!.name
                                selectCampDialog.value = false
                            }
                    ) {
                        RadioButton(
                            modifier = Modifier.size(15.dp),
                            selected = camp.name == selectedItem.value,
                            onClick = {

                                selectedItem.value = camp.name
                                selectedCamp_sharedPref.edit().putString(SELECTED_CAMPP, json2)
                                    .apply()
                                currentCampName.value = getCurrentCamp()!!.name
                                selectCampDialog.value = false
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = colorResource(id = R.color.mainColor),
                                unselectedColor = colorResource(id = R.color.mainColor),
                                disabledColor = Color.DarkGray
                            )
                        )
                        Text(
                            text = camp.name,
                            modifier = Modifier.padding(start = 10.dp),
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}