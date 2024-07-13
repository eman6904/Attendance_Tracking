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


    @GET("Mobile/getPresentTrainees")
    fun getPresentTrainees(@Query("campId") campId: Int,@Query("currentDate")currentDate:String): Call<ApiResponse>


    @PUT("Mobile/updateTraineePoints")
    fun traineePointsUpdate(
        @Body extraPoint: ExtraPoint,
        @Header("Authorization") token: String
    ): Call<ApiResponse>

    @POST("Auth/login")
    fun login(
        @Body loginData: LoginData,
    ): Call<LoginResponse>

    @POST("Auth/forget-password")
    fun forgetPassword(@Query("email")email:String):Call<ForgetPasswordResponse>

    @GET("Auth/checkResetOtp")
    fun checkOtp(@Query("Email")email:String, @Query("Otp")otp:Int):Call<ForgetPasswordResponse>

    @POST("Auth/reset-password")
    fun resetPassword(@Body resetPassword:PasswordResetRequirements):Call<ForgetPasswordResponse>
}
