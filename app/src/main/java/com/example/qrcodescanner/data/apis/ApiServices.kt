package com.example.qrcodescanner.data.apis

import com.example.qrcodescanner.data.model.*
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
        @Body traineeId: AttendanceRegistrationRequirements,
        @Header("Authorization") token: String
    ): Call<AttendanceRegistrationResponse>


    @GET("Mobile/getPresentTrainees")
    fun getPresentTrainees(
        @Query("campId") campId: Int,
        @Query("currentDate")currentDate:String,
        @Query("keyWord")keyWord:String,
        @Header("Authorization") token: String
    ): Call<ApiResponse>


    @PUT("Mobile/updateTraineePoints")
    fun traineePointsUpdate(
        @Body extraPoint: ExtraPointRequirements,
        @Header("Authorization") token: String
    ): Call<ApiResponse>

    @POST("Auth/mobile-login")
    fun login(
        @Body loginData: LoginRequirements,
    ): Call<LoginResponse>

    @POST("Auth/forget-password")
    fun forgetPassword(@Query("email")email:String):Call<PasswordResetResponse>

    @GET("Auth/checkResetOtp")
    fun checkOtp(@Query("Email")email:String, @Query("Otp")otp:Int):Call<PasswordResetResponse>

    @POST("Auth/reset-password")
    fun resetPassword(@Body resetPassword:PasswordResetRequirements):Call<PasswordResetResponse>
}