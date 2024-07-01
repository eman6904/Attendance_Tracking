package com.example.qrcodescanner

sealed class ScreensRoute(val route:String){
    object LogInScreen: ScreensRoute(route="login")
    object MainScreen: ScreensRoute(route="mainScreen")
    object ExtraPointScreen: ScreensRoute(route="extraPoint")
    object AttendanceScreen: ScreensRoute(route="attendance")
}