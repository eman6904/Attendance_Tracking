package com.example.qrcodescanner

import QrScannerScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.qrcodescanner.Back.DataClasses.ApiResponse
import com.example.qrcodescanner.Back.DataClasses.AttendanceResponse
import com.example.qrcodescanner.Back.DataClasses.LoginRequirements
import com.example.qrcodescanner.Front.*
import com.google.gson.Gson

@Composable
fun appNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = ScreensRoute.LogInScreen.route

    ) {
        composable(route = ScreensRoute.LogInScreen.route) { login2(navController) }
        composable(route = ScreensRoute.ExtraPointScreen.route) { extraPoints(navController) }
        composable(route = ScreensRoute.MainScreen.route) { mainScreen(navController) }
        composable(route = ScreensRoute.AttendanceScreen.route) { attendanceList(navController) }
        composable(route = ScreensRoute.ScannerScreen.route) { QrScannerScreen(navController) }
        composable(route = ScreensRoute.TraineeScreen.route+"/{apiResponse}") {
            val json = it.arguments?.getString("apiResponse")
            val apiResponse = Gson().fromJson(json, AttendanceResponse::class.java)
            traineeScreen(navController,apiResponse)
        }
        composable(route = ScreensRoute.NewPasswordScreen.route+"/{email}"+"/{token}") {
            val email=it.arguments?.getString("email")
            val token=it.arguments?.getString("token")
            newPassword(navController,email.toString(),token.toString())
        }
        composable(route = ScreensRoute.ForgetPasswordScreen.route) { forgetPassword(navController) }
        composable(route = ScreensRoute.OTPCodeScreen.route+"/{email}") {
            val email=it.arguments?.getString("email")
            OTPCode(navController,email.toString())
        }
    }
}