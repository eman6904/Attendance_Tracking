package com.example.qrcodescanner.coding.APIs
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrcodescanner.coding.DataClasses.ExtraPoint
import com.example.qrcodescanner.coding.DataClasses.ApiResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModel:ViewModel() {

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

    fun getAllTrainees(campId:Int,token:String):LiveData<ApiResponse>{

        connect.connect().getTraineesByCampId(campId,token).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {

                val trainee=response.body()
                if(response.isSuccessful) {
                    _traineeResponse.postValue(trainee)
                    Log.d("trainees","${trainee!!.data}")

                }else{
                    Log.d("problem","${response.code()}")
                }

            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d("errror", t.message.toString())
            }

        })

        return traineeResponse
    }

    fun getCamps(token:String):LiveData<ApiResponse>{

        connect.connect().getAllCamps(token).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {

                val camp=response.body()
                if(response.isSuccessful) {
                    _campsResponse.postValue(camp)
                    Log.d("trainees","${camp!!.data}")

                }else{
                    Log.d("problem","${response.code()}")
                }

            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d("errror", t.message.toString())
            }
        })
        return campsResponse
    }

    fun addTraineeToAttendance(traineeId:String,token:String):LiveData<ApiResponse>{

        connect.connect().addTraineeToAttendance(traineeId,token).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {

                val responseForAddTrainee=response.body()
                if(response.isSuccessful) {
                    _addTraineeResponse.postValue(responseForAddTrainee)
                    Log.d("trainees","${responseForAddTrainee!!.message}")

                }else{
                    Log.d("problem","${response.code()}")
                }

            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d("errror", t.message.toString())
            }

        })

        return addTraineeResponse
    }

    fun getPresentTrainees(CampId:Int):LiveData<ApiResponse>{

        connect.connect().getPresentTrainees(CampId).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {

                if(response.isSuccessful) {
                    _presentTraineeResponse.postValue(response.body())
                    Log.d("data","${response.body()!!.data}")

                }else{
                    Log.d("problem","${response.code()}")
                }

            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d("errror", t.message.toString())
            }

        })
        return presentTraineeResponse
    }

    fun traineePointsUpdate(extraPoint: ExtraPoint,token:String):LiveData<ApiResponse>{

        connect.connect().traineePointsUpdate(extraPoint,token).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {

                val pointsUpdateResponse2=response.body()
                if(response.isSuccessful) {
                    _pointsUpdateResponse.postValue(pointsUpdateResponse2)
                    Log.d("message","${pointsUpdateResponse2!!.message}")

                }else{
                    Log.d("problem","${response.code()}")
                }

            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d("errror", t.message.toString())
            }

        })
        return pointsUpdateResponse
    }

}

