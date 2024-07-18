package com.example.qrcodescanner.Back.functions

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.qrcodescanner.MainActivity
import com.example.qrcodescanner.MainActivity.Companion.SELECTED_USER
import com.example.qrcodescanner.MainActivity.Companion.connect
import com.example.qrcodescanner.MainActivity.Companion.token
import com.example.qrcodescanner.R
import com.example.qrcodescanner.ScreensRoute
import com.example.qrcodescanner.Back.DataClasses.*
import com.example.qrcodescanner.Front.space
import com.example.qrcodescanner.MainActivity.Companion.LOGIN_REQUIREMENTS
import com.example.qrcodescanner.MainActivity.Companion.MODE
import com.example.qrcodescanner.MainActivity.Companion.REMEMBER_ME
import com.example.qrcodescanner.MainActivity.Companion.rememberMe_sharedPref
import com.example.qrcodescanner.MainActivity.Companion.userData_sharedPref
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

fun getAllTrainees(
    trainees: MutableList<ItemData>,
    showProgress: MutableState<Boolean>,
    shutDownError: MutableState<Boolean>,
    errorMessage: MutableState<String>,
    noTrainee:MutableState<Boolean>
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
                        showProgress.value=true
                        if (response.isSuccessful) {

                            if (response.body()!!.data.isEmpty())
                                noTrainee.value = true
                            else
                                trainees.addAll(response!!.body()!!.data)
                            showProgress.value=false
                        } else {

                            shutDownError.value = true
                            errorMessage.value = response.message()
                            showProgress.value = false
                        }

                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {

                        shutDownError.value = true
                        errorMessage.value = "Network error: " +"Please check your internet connection"
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
    selectedTrainee:MutableState<String>
) {

    val campId = getCurrentCamp()?.id?.toInt()
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    var currentDate = sdf.format(Date())

    GlobalScope.launch(Dispatchers.IO) {
        try {
            connect.connect().getPresentTrainees(campId!!, currentDate,selectedTrainee.value,"Bearer $token")
                .enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(
                        call: Call<ApiResponse>,
                        response: Response<ApiResponse>
                    ) {

                        trainees.clear()
                        itemsCase.value = ""
                        showProgress.value = true

                        if (response.isSuccessful) {
                           // Log.d("response", "${response.body()}")
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
                            errorMessage.value = response.message()
                            showProgress.value = false
                        }

                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {

                        shutDownError.value = true
                        errorMessage.value = "Network error: " +"Please check your internet connection"
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
                    showProgress.value=true
                    if (response.isSuccessful) {

                        if (response.body()!!.data == null)
                            itemsCase.value = "No Camps"
                        camps.addAll(response.body()!!.data)
                        if(itemsCase.value.isNotEmpty()||camps.isNotEmpty())
                        showProgress.value = false

                    } else {
                        shutDownError.value = true
                        errorMessage.value = response.message()
                        showProgress.value = false
                    }

                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {


                    shutDownError.value = true
                    errorMessage.value = "Network error: " +"Please check your internet connection"
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
    traineeRequirements: AddTraineeRequirements,
    isSuccess: MutableState<Boolean>,
    message: MutableState<String>,
    shutDown: MutableState<Boolean>,
    shutDownError: MutableState<Boolean>,
    errorMessage: MutableState<String>,
) {
    GlobalScope.launch(Dispatchers.IO) {
        try {
            connect.connect().addTraineeToAttendance(traineeRequirements, "Bearer $token")
                .enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(
                        call: Call<ApiResponse>,
                        response: Response<ApiResponse>
                    ) {
                        val result = response.body()

                        if (response.isSuccessful) {

                            message.value = result!!.message
                            isSuccess.value = result.isSuccess
                            shutDown.value = true

                        } else {
                            shutDownError.value = true
                            errorMessage.value = response.message()
                        }

                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        shutDownError.value = true
                        errorMessage.value = "Network error: " + t.message.toString()
                    }

                })
        } catch (e: Exception) {

            shutDownError.value = true
            errorMessage.value = "Unexpected error: " + e.message.toString()
        }
    }
}

fun traineePointsUpdate(extraPoint: ExtraPointRequirements,
                        isSuccess: MutableState<Boolean>,
                        message: MutableState<String>,
                        shutDownError: MutableState<Boolean>,
                        errorMessage: MutableState<String>,) {
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
                            errorMessage.value = response.message()
                        }

                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {

                        shutDownError.value = true
                        errorMessage.value = "Network error: " +"Please check your internet connection"
                    }

                })
        } catch (e: Exception) {

            shutDownError.value = true
            errorMessage.value = "Unexpected error: " + e.message.toString()
        }
    }

}

fun forgotPassword(email: String,
  isSuccess: MutableState<Boolean>,
  message: MutableState<List<String>>,
  shutDownError: MutableState<Boolean>,
  errorMessage: MutableState<String>,
  showProgress: MutableState<Boolean>){

    GlobalScope.launch(Dispatchers.IO) {
        try {
            connect.connect().forgetPassword(email)
                .enqueue(object : Callback<ForgetPasswordResponse> {
                    override fun onResponse(
                        call: Call<ForgetPasswordResponse>,
                        response: Response<ForgetPasswordResponse>
                    ) {
                        message.value= listOf()
                        val apiResponse = response.body()
                        isSuccess.value=false

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
                            errorMessage.value = response.message()
                            showProgress.value=false
                        }

                    }

                    override fun onFailure(call: Call<ForgetPasswordResponse>, t: Throwable) {

                        shutDownError.value = true
                        errorMessage.value = "Network error: " +"Please check your internet connection"
                        showProgress.value=false
                    }

                })
        } catch (e: Exception) {

            shutDownError.value = true
            errorMessage.value = "Unexpected error: " + e.message.toString()
            showProgress.value=false
        }
    }

}

fun checkOtp(email: String,
             otp: Int,
             isSuccess: MutableState<Boolean>,
             message: MutableState<List<String>>,
             token2: MutableState<String>,
             shutDownError: MutableState<Boolean>,
             errorMessage: MutableState<String>,
             showProgress: MutableState<Boolean>) {

    GlobalScope.launch(Dispatchers.IO) {
        try {
            connect.connect().checkOtp(email, otp)
                .enqueue(object : Callback<ForgetPasswordResponse> {
                    override fun onResponse(
                        call: Call<ForgetPasswordResponse>,
                        response: Response<ForgetPasswordResponse>
                    ) {

                        val apiResponse = response.body()
                        message.value = listOf()
                        isSuccess.value=false

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
                            errorMessage.value = response.message()
                            showProgress.value = false
                        }

                    }

                    override fun onFailure(call: Call<ForgetPasswordResponse>, t: Throwable) {

                        shutDownError.value = true
                        errorMessage.value = "Network error: " +"Please check your internet connection"
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
fun resetPassword( resetRequirements: PasswordResetRequirements,
                   isSuccess: MutableState<Boolean>,
                   message: MutableState<List<String>>,
                   shutDownError: MutableState<Boolean>,
                   errorMessage: MutableState<String>,
                   showProgress: MutableState<Boolean>) {

    GlobalScope.launch(Dispatchers.IO) {
        try {
            connect.connect().resetPassword(resetRequirements)
                .enqueue(object : Callback<ForgetPasswordResponse> {
                    override fun onResponse(
                        call: Call<ForgetPasswordResponse>,
                        response: Response<ForgetPasswordResponse>
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
                            errorMessage.value = response.message()
                            showProgress.value = false
                        }

                    }

                    override fun onFailure(call: Call<ForgetPasswordResponse>, t: Throwable) {

                        shutDownError.value = true
                        errorMessage.value = "Network error: " +"Please check your internet connection"
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
                           // Log.d("responsee","${response.body()}")
                            val json2 = gson.toJson(loginResponse.data)
                            MainActivity.selectedUser_sharedPref.edit()
                                .putString(MainActivity.SELECTED_USER, json2).apply()

                            val json3=gson.toJson(loginData)
                            userData_sharedPref.edit().putString(LOGIN_REQUIREMENTS,json3).apply()

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
                        errorMessage.value = response.message()
                        showProgress.value = false
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                    shutDownError.value = true
                    errorMessage.value = "Network error: " +"Please check your internet connection"
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

fun getCurrentUser(): UserData? {

    val gson = Gson()
    val json = MainActivity.selectedUser_sharedPref.getString(SELECTED_USER, null)
    val currentUser = json?.let { gson.fromJson(it, UserData::class.java) }

    return currentUser
}

@Composable
fun errorDialog(
    shutDown:MutableState<Boolean>,
    errorMessage:MutableState<String>) {

    if(shutDown.value){
        Dialog(
            onDismissRequest = {
                shutDown.value=false
            }) {
            Card(
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp),
                elevation = 10.dp
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "Error!",
                        fontSize = 25.sp,
                        fontFamily = FontFamily(Font(R.font.bold2)),

                        )
                    space(h = 10)
                    Text(
                        text = errorMessage.value
                    )
                    space(10)
                    Button(
                        onClick = {
                            shutDown.value=false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors= ButtonDefaults.buttonColors(
                            backgroundColor = Color.Red
                        )
                    ) {
                        Text(
                            text = "OK",
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.bold2))
                        )
                    }
                }
            }
        }
    }
}
fun logout(
    shutDownError: MutableState<Boolean>,
    errorMessage: MutableState<String>,
    navController: NavHostController,
) {
        try {
            rememberMe_sharedPref.edit().putString(REMEMBER_ME,null).apply()
            navController.navigate(ScreensRoute.LogInScreen.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }

        } catch (e: Exception) {
            shutDownError.value = true
            errorMessage.value = "Unexpected error: " + e.message.toString()
        }
}
fun getCurrentCamp(): ItemData? {

    val gson = Gson()
    val json = MainActivity.selectedCamp_sharedPref.getString(MainActivity.SELECTED_CAMPP, null)
    val savedCamp = json?.let { gson.fromJson(it, ItemData::class.java) }

    return savedCamp
}
fun getLoginData(): LoginRequirements? {

    val gson = Gson()
    val json = MainActivity.userData_sharedPref.getString(MainActivity.LOGIN_REQUIREMENTS, null)
    val savedData = json?.let { gson.fromJson(it, LoginRequirements::class.java) }

    return savedData
}

fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy")
    var currentDate = sdf.format(Date())

    return currentDate
}
fun getMode():String?{

   return MainActivity.mode_sharedPref.getString(MainActivity.MODE,null)
}
fun refresh(context:Context){

    val refreshIntent = Intent(context, MainActivity::class.java)
    refreshIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(refreshIntent)
}

//كنت مستخدمة live data ولكن تراجعت عن ذلك لسوء التعامل معها حيث يحدث تاخر ف عمليه data observation
// وبالتالي عند عرض الداتا فانه يقوم بجلب الداتا القديمة اولا ثم الحديثة