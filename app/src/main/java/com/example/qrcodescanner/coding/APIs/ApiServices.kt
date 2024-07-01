package com.example.qrcodescanner.coding.APIs

import com.example.qrcodescanner.coding.DataClasses.*
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {
    @GET("Mobile/getTraineesByCampId/{campId}")
    fun getTraineesByCampId(
        @Path("campId") campId: Int,
        @Header("Authorization") token: String
    ): Call<ApiResponse>


    @GET("Mobile/getCamps")
    fun getAllCamps(@Header("Authorization") token: String): Call<ApiResponse>

    @POST("Mobile/addTraineeToAttendence")
    fun addTraineeToAttendance(
        @Body traineeId: String,
        @Header("Authorization") token: String
    ): Call<ApiResponse>


    @GET("Mobile/getPresentTrainees/{campId}")
    fun getPresentTrainees(@Path("campId") campId: Int): Call<ApiResponse>


    @PUT("Mobile/updateTraineePoints")
    fun traineePointsUpdate(
        @Body extraPoint: ExtraPoint,
        @Header("Authorization") token: String
    ): Call<ApiResponse>


}
