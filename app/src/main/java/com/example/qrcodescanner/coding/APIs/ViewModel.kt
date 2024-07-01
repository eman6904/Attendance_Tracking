package com.example.qrcodescanner.coding.APIs
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qrcodescanner.coding.DataClasses.ExtraPoint
import com.example.qrcodescanner.coding.DataClasses.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModel:ViewModel() {

    private val connect = ApiConnection()
    private val _traineeResponse = MutableLiveData<ApiResponse>()
    val traineeResponse: MutableLiveData<ApiResponse> get() = _traineeResponse

    private val _campsResponse = MutableLiveData<ApiResponse>()
    val campsResponse: MutableLiveData<ApiResponse> get() = _campsResponse

    private val _addTraineeResponse = MutableLiveData<ApiResponse>()
    val addTraineeResponse: MutableLiveData<ApiResponse> get() = _addTraineeResponse

    private val _presentTraineesResponse = MutableLiveData<ApiResponse>()
    val presentTraineeResponse: MutableLiveData<ApiResponse> get() = _presentTraineesResponse

    private val _pointsUpdateResponse = MutableLiveData<ApiResponse>()
    val pointsUpdateResponse: MutableLiveData<ApiResponse> get() = _pointsUpdateResponse

    fun getAllTrainees(campId:Int,token:String):LiveData<ApiResponse>{

        connect.connect().getTraineesByCampId(campId,token).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {

                val trainee=response.body()
                if(response.isSuccessful) {
                    traineeResponse.postValue(trainee)
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
                    campsResponse.postValue(camp)
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
                    addTraineeResponse.postValue(responseForAddTrainee)
                    Log.d("trainees","${responseForAddTrainee!!.data}")

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

                val presentTraineesResponse=response.body()
                if(response.isSuccessful) {
                    presentTraineeResponse.postValue(presentTraineesResponse)
                    Log.d("message","${presentTraineesResponse!!.message}")

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
                    pointsUpdateResponse.postValue(pointsUpdateResponse2)
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