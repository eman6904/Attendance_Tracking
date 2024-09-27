package com.example.qrcodescanner.data.apis

import com.example.qrcodescanner.data.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiServices {
    @GET("Mobile/getTraineesByCampId/{campId}")
   suspend fun getTraineesByCampId(
        @Path("campId") campId: Int,
        @Header("Authorization") token: String
    ): Response<ApiResponse>


    @GET("Mobile/getCamps")
    suspend fun getAllCamps(@Header("Authorization") token: String): Response<ApiResponse>

    @POST("Mobile/addTraineeToAttendence")
    suspend fun addTraineeToAttendance(
        @Body traineeId: AttendanceRegistrationRequirements,
        @Header("Authorization") token: String
    ): Response<AttendanceRegistrationResponse>


    @GET("Mobile/getPresentTrainees")
    suspend fun getPresentTrainees(
        @Query("campId") campId: Int,
        @Query("currentDate")currentDate:String,
        @Query("keyWord")keyWord:String,
        @Header("Authorization") token: String
    ): Response<ApiResponse>


    @PUT("Mobile/updateTraineePoints")
   suspend fun traineePointsUpdate(
        @Body extraPoint: ExtraPointRequirements,
        @Header("Authorization") token: String
    ): Response<ApiResponse>

    @POST("Auth/mobile-login")
    suspend fun login(
        @Body loginData: LoginRequirements,
    ): Response<LoginResponse>

    @POST("Auth/forget-password")
   suspend fun forgetPassword(@Query("email")email:String):Response<PasswordResetResponse>

    @GET("Auth/checkResetOtp")
    suspend fun checkOtp(@Query("Email")email:String, @Query("Otp")otp:Int):Response<PasswordResetResponse>

    @POST("Auth/reset-password")
   suspend fun resetPassword(@Body resetPassword:PasswordResetRequirements):Response<PasswordResetResponse>
}