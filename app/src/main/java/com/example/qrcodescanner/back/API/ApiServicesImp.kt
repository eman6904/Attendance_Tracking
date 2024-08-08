package com.example.qrcodescanner.back.API

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import com.example.qrcodescanner.MainActivity
import com.example.qrcodescanner.MainActivity.Companion.LOGIN_REQUIREMENTS
import com.example.qrcodescanner.MainActivity.Companion.connect
import com.example.qrcodescanner.MainActivity.Companion.token
import com.example.qrcodescanner.MainActivity.Companion.userData_sharedPref
import com.example.qrcodescanner.back.Model.AttendanceRegistrationRequirements
import com.example.qrcodescanner.back.Model.ApiResponse
import com.example.qrcodescanner.back.Model.AttendanceRegistrationResponse
import com.example.qrcodescanner.back.Model.ExtraPointRequirements
import com.example.qrcodescanner.back.Model.PasswordResetResponse
import com.example.qrcodescanner.back.Model.ItemData
import com.example.qrcodescanner.back.Model.LoginRequirements
import com.example.qrcodescanner.back.Model.LoginResponse
import com.example.qrcodescanner.back.Model.PasswordResetRequirements
import com.example.qrcodescanner.back.Model.UserData
import com.example.qrcodescanner.back.function.getCurrentCamp
import com.example.qrcodescanner.back.function.getCurrentUser
import com.example.qrcodescanner.navigation.ScreensRoute
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getAllTrainees(
    trainees: MutableList<ItemData>,
    showProgress: MutableState<Boolean>,
    shutDownError: MutableState<Boolean>,
    errorMessage: MutableState<String>,
    noTrainee: MutableState<Boolean>
) {

    val campId = getCurrentCamp()?.id?.toInt()
    GlobalScope.launch(Dispatchers.IO) {

        try {
            connect.connect().getTraineesByCampId(campId!!, "Bearer $token")
                .enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(
                        call: Call<ApiResponse>,
                        response: Response<ApiResponse>
                    ) {

                        trainees.clear()
                        noTrainee.value = false
                        showProgress.value = true
                        if (response.isSuccessful) {

                            if (response.body()!!.data.isEmpty())
                                noTrainee.value = true
                            else
                                trainees.addAll(response!!.body()!!.data)
                            showProgress.value = false
                        } else {

                            shutDownError.value = true
                            errorMessage.value =
                                "There was a problem connecting to the server. Try again"
                            showProgress.value = false
                        }

                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {

                        shutDownError.value = true
                        errorMessage.value =
                            "Please check your internet connection"
                        showProgress.value = false
                    }

                })
        } catch (e: Exception) {

            shutDownError.value = true
            errorMessage.value = "Unexpected error: " + e.message.toString()
            showProgress.value = false
        }

    }
}

fun getPresentTrainees(
    itemsCase: MutableState<String>,
    trainees: MutableList<ItemData>,
    showProgress: MutableState<Boolean>,
    shutDownError: MutableState<Boolean>,
    errorMessage: MutableState<String>,
    selectedTrainee: MutableState<String>
) {

    val campId = getCurrentCamp()?.id?.toInt()
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
    var currentDate = sdf.format(Date())

    GlobalScope.launch(Dispatchers.IO) {
        try {
            connect.connect()
                .getPresentTrainees(campId!!, currentDate, selectedTrainee.value, "Bearer $token")
                .enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(
                        call: Call<ApiResponse>,
                        response: Response<ApiResponse>
                    ) {

                        trainees.clear()
                        itemsCase.value = ""
                        showProgress.value = true
                        Log.d("api_response", response.body().toString())
                        if (response.isSuccessful) {

                            if (response.body()!!.message == "No current session for now.")
                                itemsCase.value = "No current session for now."
                            else {
                                if (response.body()!!.data.size == 0) {
                                    itemsCase.value = "No trainee has presented yet"
                                    showProgress.value = false
                                } else {
                                    response.body()?.data?.let {
                                        trainees.addAll(it)
                                        showProgress.value = false
                                    }

                                }
                            }
                            showProgress.value = false
                        } else {

                            shutDownError.value = true
                            errorMessage.value =
                                "There was a problem connecting to the server. Try again"
                            showProgress.value = false
                        }

                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {

                        shutDownError.value = true
                        errorMessage.value =
                            "Please check your internet connection"
                        showProgress.value = false
                    }
                })
        } catch (e: Exception) {

            shutDownError.value = true
            errorMessage.value = "Unexpected error: " + e.message.toString()
            showProgress.value = false
        }
    }
}

fun getCamps(
    camps: MutableList<ItemData>,
    itemsCase: MutableState<String>,
    showProgress: MutableState<Boolean>,
    shutDownError: MutableState<Boolean>,
    errorMessage: MutableState<String>,
) {
    GlobalScope.launch(Dispatchers.IO) {
        try {

            connect.connect().getAllCamps("Bearer $token").enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {

                    camps.clear()
                    itemsCase.value = ""
                    showProgress.value = true
                    if (response.isSuccessful) {

                        if (response.body()!!.data == null)
                            itemsCase.value = "No Camps"
                        camps.addAll(response.body()!!.data)
                        if (itemsCase.value.isNotEmpty() || camps.isNotEmpty())
                            showProgress.value = false

                    } else {
                        shutDownError.value = true
                        errorMessage.value =
                            "There was a problem connecting to the server. Try again"
                        showProgress.value = false
                    }

                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {


                    shutDownError.value = true
                    errorMessage.value = "Please check your internet connection"
                    showProgress.value = false
                }
            })
        } catch (e: Exception) {

            shutDownError.value = true
            errorMessage.value = "Unexpected error: " + e.message.toString()
            showProgress.value = false
        }
    }

}

fun addTraineeToAttendance(
    traineeRequirements: AttendanceRegistrationRequirements,
    navController: NavHostController,
    shutDownError: MutableState<Boolean>,
    errorMessage: MutableState<String>,
    message: MutableState<String>,
    shutDownFailedDialog: MutableState<Boolean>
) {
    GlobalScope.launch(Dispatchers.IO) {
        try {
            connect.connect().addTraineeToAttendance(traineeRequirements, "Bearer $token")
                .enqueue(object : Callback<AttendanceRegistrationResponse> {
                    override fun onResponse(
                        call: Call<AttendanceRegistrationResponse>,
                        response: Response<AttendanceRegistrationResponse>
                    ) {
                        message.value = ""
                        shutDownFailedDialog.value = false

                        if (response.isSuccessful) {

                            val apiResponseBody = response.body()!!
                            if (apiResponseBody != null) {
                                val apiResponseJson = Uri.encode(Gson().toJson(apiResponseBody))
                                if (!apiResponseBody.isSuccess) {
                                    shutDownFailedDialog.value = true
                                    message.value = apiResponseBody.message
                                } else {
                                    navController.navigate(ScreensRoute.TraineeScreen.route + "/${apiResponseJson}")
                                }
                            } else {
                                shutDownError.value = true
                                errorMessage.value = "Response body is null"
                            }
                        } else {
                            shutDownError.value = true
                            errorMessage.value =
                                "There was a problem connecting to the server. Try again"
                        }

                    }

                    override fun onFailure(
                        call: Call<AttendanceRegistrationResponse>,
                        t: Throwable
                    ) {
                        shutDownError.value = true
                        errorMessage.value =
                            "Please check your internet connection"
                    }

                })
        } catch (e: Exception) {

            shutDownError.value = true
            errorMessage.value = "Unexpected error: " + e.message.toString()
        }
    }
}

fun traineePointsUpdate(
    extraPoint: ExtraPointRequirements,
    isSuccess: MutableState<Boolean>,
    message: MutableState<String>,
    shutDownError: MutableState<Boolean>,
    errorMessage: MutableState<String>,
) {
    GlobalScope.launch(Dispatchers.IO) {
        try {
            connect.connect().traineePointsUpdate(extraPoint, "Bearer $token")
                .enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(
                        call: Call<ApiResponse>,
                        response: Response<ApiResponse>
                    ) {

                        if (response.isSuccessful) {

                            message.value = response.body()!!.message
                            isSuccess.value = response.body()!!.isSuccess

                        } else {

                            shutDownError.value = true
                            errorMessage.value =
                                "There was a problem connecting to the server. Try again"
                        }

                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {

                        shutDownError.value = true
                        errorMessage.value =
                            "Please check your internet connection"
                    }

                })
        } catch (e: Exception) {

            shutDownError.value = true
            errorMessage.value = "Unexpected error: " + e.message.toString()
        }
    }

}

fun forgotPassword(
    email: String,
    isSuccess: MutableState<Boolean>,
    message: MutableState<List<String>>,
    shutDownError: MutableState<Boolean>,
    errorMessage: MutableState<String>,
    showProgress: MutableState<Boolean>
) {

    GlobalScope.launch(Dispatchers.IO) {
        try {
            connect.connect().forgetPassword(email)
                .enqueue(object : Callback<PasswordResetResponse> {
                    override fun onResponse(
                        call: Call<PasswordResetResponse>,
                        response: Response<PasswordResetResponse>
                    ) {
                        message.value = listOf()
                        val apiResponse = response.body()
                        isSuccess.value = false

                        if (response.isSuccessful) {
                            isSuccess.value = apiResponse!!.isSuccess
                            val newMessages = mutableListOf<String>()

                            if (apiResponse.message.isNotEmpty()) {
                                newMessages.add(apiResponse!!.message)
                            } else {
                                apiResponse.errors?.let { errors ->
                                    for ((key, value) in apiResponse.errors) {

                                        for (ms in value)
                                            newMessages.add(ms)
                                    }
                                }
                            }
                            message.value = newMessages
                        } else {

                            shutDownError.value = true
                            errorMessage.value =
                                "There was a problem connecting to the server. Try again"
                            showProgress.value = false
                        }

                    }

                    override fun onFailure(call: Call<PasswordResetResponse>, t: Throwable) {

                        shutDownError.value = true
                        errorMessage.value =
                            "Please check your internet connection"
                        showProgress.value = false
                    }

                })
        } catch (e: Exception) {

            shutDownError.value = true
            errorMessage.value = "Unexpected error: " + e.message.toString()
            showProgress.value = false
        }
    }

}

fun checkOtp(
    email: String,
    otp: Int,
    isSuccess: MutableState<Boolean>,
    message: MutableState<List<String>>,
    token2: MutableState<String>,
    shutDownError: MutableState<Boolean>,
    errorMessage: MutableState<String>,
    showProgress: MutableState<Boolean>
) {

    GlobalScope.launch(Dispatchers.IO) {
        try {
            connect.connect().checkOtp(email, otp)
                .enqueue(object : Callback<PasswordResetResponse> {
                    override fun onResponse(
                        call: Call<PasswordResetResponse>,
                        response: Response<PasswordResetResponse>
                    ) {

                        val apiResponse = response.body()
                        message.value = listOf()
                        isSuccess.value = false
                        // Log.d("otpResponse",apiResponse.toString())
                        if (response.isSuccessful) {

                            isSuccess.value = apiResponse!!.isSuccess
                            val newMessages = mutableListOf<String>()

                            if (apiResponse.isSuccess)
                                token2.value = apiResponse.data.toString()
                            if (apiResponse.message.isNotEmpty()) {
                                newMessages.add(apiResponse.message)
                            } else {
                                apiResponse.errors?.let { errors ->
                                    for ((key, value) in apiResponse.errors) {

                                        for (ms in value)
                                            newMessages.add(ms)
                                    }
                                }
                            }
                            message.value = newMessages
                        } else {

                            shutDownError.value = true
                            errorMessage.value =
                                "There was a problem connecting to the server. Try again"
                            showProgress.value = false
                        }

                    }

                    override fun onFailure(call: Call<PasswordResetResponse>, t: Throwable) {

                        shutDownError.value = true
                        errorMessage.value =
                            "Please check your internet connection"
                        showProgress.value = false
                    }

                })
        } catch (e: Exception) {

            shutDownError.value = true
            errorMessage.value = "Unexpected error: " + e.message.toString()
            showProgress.value = false
        }
    }
}

fun resetPassword(
    resetRequirements: PasswordResetRequirements,
    isSuccess: MutableState<Boolean>,
    message: MutableState<List<String>>,
    shutDownError: MutableState<Boolean>,
    errorMessage: MutableState<String>,
    showProgress: MutableState<Boolean>
) {

    GlobalScope.launch(Dispatchers.IO) {
        try {
            connect.connect().resetPassword(resetRequirements)
                .enqueue(object : Callback<PasswordResetResponse> {
                    override fun onResponse(
                        call: Call<PasswordResetResponse>,
                        response: Response<PasswordResetResponse>
                    ) {
                        val apiResponse = response.body()
                        isSuccess.value = false
                        message.value = listOf()

                        if (response.isSuccessful) {

                            isSuccess.value = apiResponse!!.isSuccess
                            val newMessages = mutableListOf<String>()

                            if (apiResponse.message.isNotEmpty()) {
                                newMessages.add(apiResponse.message)
                            } else {
                                apiResponse.errors?.let { errors ->
                                    for ((key, value) in apiResponse.errors) {

                                        for (ms in value)
                                            newMessages.add(ms)
                                    }
                                }
                            }
                            message.value = newMessages

                        } else {

                            shutDownError.value = true
                            errorMessage.value =
                                "There was a problem connecting to the server. Try again"
                            showProgress.value = false
                        }

                    }

                    override fun onFailure(call: Call<PasswordResetResponse>, t: Throwable) {

                        shutDownError.value = true
                        errorMessage.value =
                            "Please check your internet connection"
                        showProgress.value = false
                    }

                })
        } catch (e: Exception) {

            shutDownError.value = true
            errorMessage.value = "Unexpected error: " + e.message.toString()
            showProgress.value = false
        }
    }

}

fun login(
    loginData: LoginRequirements,
    shutDownError: MutableState<Boolean>,
    errorMessage: MutableState<String>,
    showProgress: MutableState<Boolean>,
    navController: NavHostController,
    userData: MutableState<UserData>
) {

    GlobalScope.launch(Dispatchers.IO) {
        try {
            connect.connect().login(loginData).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    val gson = Gson()
                    if (response.isSuccessful) {

                        val loginResponse = response.body()
                        val result = loginResponse!!.message

                        if (result == "Success") {
                            //  Log.d("responsee", "${response.body()}")
                            val json2 = gson.toJson(loginResponse.data)
                            MainActivity.selectedUser_sharedPref.edit()
                                .putString(MainActivity.SELECTED_USER, json2).apply()

                            val json3 = gson.toJson(loginData)
                            userData_sharedPref.edit().putString(LOGIN_REQUIREMENTS, json3).apply()

                            if (getCurrentUser()?.token == loginResponse.data!!.token)
                                navController.navigate(ScreensRoute.MainScreen.route)

                            loginResponse?.data?.let {
                                userData.value = it
                            }

                        } else {

                            shutDownError.value = true
                            errorMessage.value = result
                            showProgress.value = false
                        }

                    } else {
                        shutDownError.value = true
                        errorMessage.value =
                            "There was a problem connecting to the server. Try again"
                        showProgress.value = false
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                    shutDownError.value = true
                    errorMessage.value = "Please check your internet connection"
                    showProgress.value = false
                }
            })
        } catch (e: Exception) {

            shutDownError.value = true
            errorMessage.value = "Unexpected error: " + e.message.toString()
            showProgress.value = false
        }
    }

}