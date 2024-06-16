package com.example.qrcodescanner

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.qrcodescanner.design.attendanceList
import com.example.qrcodescanner.design.extraPoints
import com.example.qrcodescanner.design.login2
import com.example.qrcodescanner.design.mainScreen

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
    }
}