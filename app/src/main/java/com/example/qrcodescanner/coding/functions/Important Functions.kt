package com.example.qrcodescanner.coding.functions

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.qrcodescanner.MainActivity
import com.example.qrcodescanner.MainActivity.Companion.SELECTED_USER
import com.example.qrcodescanner.MainActivity.Companion.token
import com.example.qrcodescanner.MainActivity.Companion.viewModel
import com.example.qrcodescanner.ScreensRoute
import com.example.qrcodescanner.coding.DataClasses.*
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

//data observation
fun getAllTrainees(
    trainees: MutableList<ItemData>,
    lifecycleOwner: LifecycleOwner
) {
    viewModel.viewModelScope.launch {
        val campId=getCurrentCamp()?.id?.toInt()
        viewModel.getAllTrainees(campId!!, token).observe(lifecycleOwner, Observer { apiResponse ->
            trainees.clear()
            apiResponse?.data?.let {
                trainees.addAll(it)
            }
        })
    }
}

fun getPresentTrainees(
    trainees: MutableList<ItemData>,
    itemsCase: MutableState<String>,
    lifecycleOwner: LifecycleOwner,
    showProgress: MutableState<Boolean>
) {
    viewModel.viewModelScope.launch {
        val campId=getCurrentCamp()?.id?.toInt()
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        var currentDate = sdf.format(Date())

        viewModel.getPresentTrainees(
            campId!!,
            currentDate,
            itemsCase,
            trainees,
            showProgress).observe(lifecycleOwner, Observer { apiResponse ->

        })
    }
}

fun getAllCamps(
    camps: MutableList<ItemData>,
    itemsCase: MutableState<String>,
    lifecycleOwner: LifecycleOwner
) {
    viewModel.viewModelScope.launch {
        viewModel.getCamps(token).observe(lifecycleOwner, Observer { apiResponse ->
            camps.clear()
            itemsCase.value=""
            if (apiResponse?.data == null) {
                itemsCase.value = "No Camps"
            }
            apiResponse?.data?.let {
                camps.addAll(it)
            }
        })
    }
}

fun addTrainee(
    traineeId: String,
    isSuccess: MutableState<Boolean>,
    message: MutableState<String>,
    lifecycleOwner: LifecycleOwner,
    shutDown:MutableState<Boolean>,
    showProgress: MutableState<Boolean>

) {

    viewModel.viewModelScope.launch {
        viewModel.addTraineeToAttendance(
            traineeId,
            token,
            isSuccess,
            message,
            shutDown,
            showProgress
        ).observe(lifecycleOwner, Observer { apiResponse ->

        })
    }
}

fun updatePoints(
    extraPoint: ExtraPoint,
    isSuccess: MutableState<Boolean>,
    message: MutableState<String>,
    lifecycleOwner: LifecycleOwner
) {
    viewModel.viewModelScope.launch {
        viewModel.traineePointsUpdate(extraPoint, token).observe(lifecycleOwner, Observer { apiResponse ->
            message.value = apiResponse.message
            isSuccess.value = apiResponse.isSuccess
        })
    }
}
fun forgotPassword(
    email: String,
    isSuccess: MutableState<Boolean>,
    message: MutableState<List<String>>,
    lifecycleOwner: LifecycleOwner
) {
    viewModel.viewModelScope.launch {
        viewModel.forgetPassword(email).observe(lifecycleOwner, Observer { apiResponse ->
            isSuccess.value = apiResponse.isSuccess
            val newMessages = mutableListOf<String>()

            if (apiResponse.message.isNotEmpty()) {
                newMessages.add(apiResponse.message)
            } else {
                apiResponse.errors?.get("Email")?.let { errors ->
                    newMessages.addAll(errors)
                }
            }

            message.value = newMessages
        })
    }
}

fun checkOtp(
    email: String,
    otp: Int,
    isSuccess: MutableState<Boolean>,
    message: MutableState<List<String>>,
    lifecycleOwner: LifecycleOwner,
    token2:MutableState<String>
) {
    viewModel.viewModelScope.launch {
        viewModel.checkOtp(email, otp).observe(lifecycleOwner, Observer { apiResponse ->
            isSuccess.value = apiResponse.isSuccess
            val newMessages = mutableListOf<String>()

            if(apiResponse.isSuccess)
                token2.value=apiResponse.data.toString()
            if (apiResponse.message.isNotEmpty()) {
                newMessages.add(apiResponse.message)
            } else {
                apiResponse.errors?.get("Email")?.let { errors ->
                    newMessages.addAll(errors)
                }
            }

            message.value = newMessages
        })
    }
}

fun resetPassword(
    resetRequirements: PasswordResetRequirements,
    isSuccess: MutableState<Boolean>,
    message: MutableState<List<String>>,
    lifecycleOwner: LifecycleOwner
) {
    viewModel.viewModelScope.launch {
        viewModel.resetPassword(resetRequirements).observe(lifecycleOwner, Observer { apiResponse ->
            isSuccess.value = apiResponse.isSuccess
            val newMessages = mutableListOf<String>()

            if (apiResponse.message.isNotEmpty()) {
                newMessages.add(apiResponse.message)
            } else {
                apiResponse.errors?.get("Password")?.let { errors ->
                    newMessages.addAll(errors)
                }
            }

            message.value = newMessages
        })
    }
}

fun login(
    loginData: LoginData,
    message: MutableState<String>,
    userData: MutableState<UserData>,
    lifecycleOwner: LifecycleOwner,
    navController: NavHostController
) {
    viewModel.viewModelScope.launch{
    val gson = Gson()
        viewModel.login(loginData).observe(lifecycleOwner, Observer { loginResponse ->
            message.value = loginResponse.message

            if (message.value == "Success") {
                val json2 = gson.toJson(loginResponse.data)
                MainActivity.selectedUser_sharedPref.edit().putString(SELECTED_USER, json2).apply()

                if(getCurrentUser()?.token==loginResponse.data!!.token)
                 navController.navigate(ScreensRoute.MainScreen.route)
            }

            loginResponse?.data?.let {
                userData.value = it
            }
        })
    }
}
fun getCurrentUser():UserData?{

    val gson = Gson()
    val json = MainActivity.selectedUser_sharedPref.getString(SELECTED_USER, null)
    val currentUser = json?.let { gson.fromJson(it, UserData::class.java) }

    return currentUser
}
fun getCurrentCamp():ItemData?{

    val gson = Gson()
    val json = MainActivity.selectedCamp_sharedPref.getString(MainActivity.SELECTED_CAMPP, null)
    val savedCamp=json?.let { gson.fromJson(it, ItemData::class.java) }

    return savedCamp
}
fun getCurrentDate():String{
    val sdf = SimpleDateFormat("dd-MM-yyyy")
    var currentDate = sdf.format(Date())

    return currentDate
}

