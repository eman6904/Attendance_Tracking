package com.example.qrcodescanner.coding.APIs

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrcodescanner.coding.DataClasses.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModel : ViewModel() {

    private val connect = ApiConnection()
    private val _traineeResponse = MutableLiveData<ApiResponse>()
    val traineeResponse: MutableLiveData<ApiResponse> = _traineeResponse

    private val _campsResponse = MutableLiveData<ApiResponse>()
    val campsResponse: MutableLiveData<ApiResponse> = _campsResponse

    private val _addTraineeResponse = MutableLiveData<ApiResponse>()
    val addTraineeResponse: MutableLiveData<ApiResponse> = _addTraineeResponse

    // MutableLiveData داخلي لتحديث البيانات
    private val _presentTraineeResponse = MutableLiveData<ApiResponse>()

    // LiveData عام للوصول إلى البيانات
    val presentTraineeResponse: LiveData<ApiResponse> = _presentTraineeResponse


    private val _pointsUpdateResponse = MutableLiveData<ApiResponse>()
    val pointsUpdateResponse: MutableLiveData<ApiResponse> = _pointsUpdateResponse

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: MutableLiveData<LoginResponse> = _loginResponse

    private val _forgotPassword = MutableLiveData<ForgetPasswordResponse>()
    val forgotPassword: MutableLiveData<ForgetPasswordResponse> = _forgotPassword

    private val _otpCheck = MutableLiveData<ForgetPasswordResponse>()
    val otpCheck: MutableLiveData<ForgetPasswordResponse> = _otpCheck

    private val _resetPassword = MutableLiveData<ForgetPasswordResponse>()
    val resetPasssword: MutableLiveData<ForgetPasswordResponse> = _resetPassword

    fun getAllTrainees(campId: Int, token: String): LiveData<ApiResponse> {
        viewModelScope.launch(Dispatchers.IO) {
            connect.connect().getTraineesByCampId(campId, token)
                .enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(
                        call: Call<ApiResponse>,
                        response: Response<ApiResponse>
                    ) {

                        val trainee = response.body()
                        if (response.isSuccessful) {
                            _traineeResponse.postValue(trainee)
                            // Log.d("trainees", "${trainee!!.data}")

                        } else {
                            //  Log.d("problem", "${response.code()}")
                        }

                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        // Log.d("errror", t.message.toString())
                    }

                })
        }
        return traineeResponse
    }

    fun getCamps(token: String): LiveData<ApiResponse> {
        viewModelScope.launch(Dispatchers.IO) {
            connect.connect().getAllCamps(token).enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {

                    val camp = response.body()
                    if (response.isSuccessful) {
                        _campsResponse.postValue(camp)
                        //  Log.d("trainees", "${camp!!.data}")

                    } else {
                        //  Log.d("problem", "${response.code()}")
                    }

                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    // Log.d("errror", t.message.toString())
                }
            })
        }
        return campsResponse
    }

    fun addTraineeToAttendance(
        traineeId: String,
        token: String,
        isSuccess: MutableState<Boolean>,
        message: MutableState<String>,
        shutDown:MutableState<Boolean>,
        showProgress: MutableState<Boolean>
    ): LiveData<ApiResponse> {
        viewModelScope.launch(Dispatchers.IO) {
            connect.connect().addTraineeToAttendance(traineeId, token)
                .enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(
                        call: Call<ApiResponse>,
                        response: Response<ApiResponse>
                    ) {
                        message.value=""
                        isSuccess.value=false
                        showProgress.value=true
                        val responseForAddTrainee = response.body()
                        if (response.isSuccessful) {
                            _addTraineeResponse.postValue(responseForAddTrainee)

                            message.value = response.body()!!.message
                            isSuccess.value = response.body()!!.isSuccess

                            showProgress.value=false
                          //  Log.d("response","${response.body()}")

                        } else {
                            //  Log.d("problem", "${response.code()}")
                        }

                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        // Log.d("errror", t.message.toString())
                    }

                })
        }

        return addTraineeResponse
    }

    fun getPresentTrainees(campId: Int,
                           currentDate: String,
                           itemCase:MutableState<String>,
                           trainees: MutableList<ItemData>,
                           showProgress: MutableState<Boolean>
    ): LiveData<ApiResponse> {
        viewModelScope.launch(Dispatchers.IO) {
            connect.connect().getPresentTrainees(campId, currentDate)
                .enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(
                        call: Call<ApiResponse>,
                        response: Response<ApiResponse>
                    ) {
                        trainees.clear()
                        itemCase.value=""
                        showProgress.value=true
                        if (response.isSuccessful) {
                            _presentTraineeResponse.postValue(response.body())
                            Log.d("response", "${response.body()}")
                            if(response.body()!!.message=="No current session for now.")
                                itemCase.value="No current session for now."
                            else{
                                if(response.body()!!.data.size==0) {
                                    itemCase.value = "No trainee has presented yet"
                                    showProgress.value = false
                                }
                                else{
                                    response.body()?.data?.let {
                                        trainees.addAll(it)
                                        showProgress.value=false
                                    }

                                }
                            }
                            showProgress.value=false
                        } else {
                            Log.d("problem", "${response.code()}")
                        }

                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        Log.d("errror", t.message.toString())
                    }

                })
        }
        return presentTraineeResponse
    }

    fun traineePointsUpdate(extraPoint: ExtraPoint, token: String): LiveData<ApiResponse> {
        viewModelScope.launch(Dispatchers.IO) {
            connect.connect().traineePointsUpdate(extraPoint, token)
                .enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(
                        call: Call<ApiResponse>,
                        response: Response<ApiResponse>
                    ) {

                        val pointsUpdateResponse2 = response.body()
                        if (response.isSuccessful) {
                            _pointsUpdateResponse.postValue(pointsUpdateResponse2)
                            // Log.d("message", "${pointsUpdateResponse2!!.message}")

                        } else {
                            //  Log.d("problem", "${response.code()}")
                        }

                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        // Log.d("errror", t.message.toString())
                    }

                })
        }
        return pointsUpdateResponse
    }

    fun login(loginData: LoginData): LiveData<LoginResponse> {

        viewModelScope.launch(Dispatchers.IO) {
            connect.connect().login(loginData).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {

                    if (response.isSuccessful) {
                        _loginResponse.postValue(response.body())
                        // Log.d("message", "${response.body()!!.data}")

                    } else {
                        //  Log.d("problem", "${response.code()}")
                    }

                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    //  Log.d("errror", t.message.toString())
                }

            })
        }
        return loginResponse
    }

    fun forgetPassword(email: String): LiveData<ForgetPasswordResponse> {

        viewModelScope.launch(Dispatchers.IO) {
            connect.connect().forgetPassword(email)
                .enqueue(object : Callback<ForgetPasswordResponse> {
                    override fun onResponse(
                        call: Call<ForgetPasswordResponse>,
                        response: Response<ForgetPasswordResponse>
                    ) {

                        if (response.isSuccessful) {
                            _forgotPassword.postValue(response.body())
                            Log.d("message", "${response.body()}")

                        } else {
                            //  Log.d("problem", "${response.code()}")
                        }

                    }

                    override fun onFailure(call: Call<ForgetPasswordResponse>, t: Throwable) {
                        //  Log.d("errror", t.message.toString())
                    }

                })
        }
        return forgotPassword
    }

    fun checkOtp(email: String, otp: Int): LiveData<ForgetPasswordResponse> {

        viewModelScope.launch(Dispatchers.IO) {
            connect.connect().checkOtp(email, otp)
                .enqueue(object : Callback<ForgetPasswordResponse> {
                    override fun onResponse(
                        call: Call<ForgetPasswordResponse>,
                        response: Response<ForgetPasswordResponse>
                    ) {

                        if (response.isSuccessful) {
                            _otpCheck.postValue(response.body())
                            Log.d("otpResponse", "${response.body()}")

                        } else {
                            //  Log.d("problem", "${response.code()}")
                        }

                    }

                    override fun onFailure(call: Call<ForgetPasswordResponse>, t: Throwable) {
                        //  Log.d("errror", t.message.toString())
                    }

                })
        }
        return otpCheck
    }

    fun resetPassword(resetRequirements: PasswordResetRequirements): LiveData<ForgetPasswordResponse> {

        viewModelScope.launch(Dispatchers.IO) {
            connect.connect().resetPassword(resetRequirements)
                .enqueue(object : Callback<ForgetPasswordResponse> {
                    override fun onResponse(
                        call: Call<ForgetPasswordResponse>,
                        response: Response<ForgetPasswordResponse>
                    ) {

                        if (response.isSuccessful) {
                            _resetPassword.postValue(response.body())
                             Log.d("message", "${response.body()}")

                        } else {
                            //  Log.d("problem", "${response.code()}")
                        }

                    }

                    override fun onFailure(call: Call<ForgetPasswordResponse>, t: Throwable) {
                        //  Log.d("errror", t.message.toString())
                    }

                })
        }
        return resetPasssword
    }
}



