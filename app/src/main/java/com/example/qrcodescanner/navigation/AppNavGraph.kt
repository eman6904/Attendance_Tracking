package com.example.qrcodescanner.navigation

import QrScannerScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.qrcodescanner.data.model.AttendanceRegistrationResponse
import com.example.qrcodescanner.ui.screens.trainee.AttendanceScreen
import com.example.qrcodescanner.ui.screens.trainee.ExtraPointsScreen
import com.example.qrcodescanner.ui.screens.auth.ForgotPasswordScreen
import com.example.qrcodescanner.ui.screens.auth.LoginScreen
import com.example.qrcodescanner.ui.screens.trainee.MainScreen
import com.example.qrcodescanner.ui.screens.auth.OTPCode
import com.example.qrcodescanner.ui.screens.auth.ResetPasswordScreen
import com.example.qrcodescanner.ui.screens.auth.SplashScreen
import com.example.qrcodescanner.ui.screens.trainee.TraineeScreen
import com.google.gson.Gson

@Composable
fun appNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = ScreensRoute.SplashScreen.route

    ) {

        composable(route = ScreensRoute.LogInScreen.route) { LoginScreen(navController) }
        composable(route = ScreensRoute.ExtraPointScreen.route) { ExtraPointsScreen(navController) }
        composable(route = ScreensRoute.MainScreen.route) { MainScreen(navController) }
        composable(route = ScreensRoute.AttendanceScreen.route) { AttendanceScreen(navController) }
        composable(route = ScreensRoute.ScannerScreen.route) { QrScannerScreen(navController) }
        composable(route = ScreensRoute.TraineeScreen.route+"/{apiResponse}") {
            val json = it.arguments?.getString("apiResponse")
            val apiResponse = Gson().fromJson(json, AttendanceRegistrationResponse::class.java)
            TraineeScreen(navController,apiResponse)
        }
        composable(route = ScreensRoute.NewPasswordScreen.route+"/{email}"+"/{token}") {
            val email=it.arguments?.getString("email")
            val token=it.arguments?.getString("token")
            ResetPasswordScreen(navController,email.toString(),token.toString())
        }
        composable(route = ScreensRoute.ForgetPasswordScreen.route) { ForgotPasswordScreen(navController) }
        composable(route = ScreensRoute.OTPCodeScreen.route+"/{email}") {
            val email=it.arguments?.getString("email")
            OTPCode(navController,email.toString())
        }
        composable(route = ScreensRoute.SplashScreen.route) { SplashScreen(navController) }
   }
}