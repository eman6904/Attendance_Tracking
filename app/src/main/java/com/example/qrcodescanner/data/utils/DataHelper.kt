package com.example.qrcodescanner.data.utils

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import com.example.qrcodescanner.MainActivity
import com.example.qrcodescanner.MainActivity.Companion.SELECTED_USER
import com.example.qrcodescanner.navigation.ScreensRoute
import com.example.qrcodescanner.data.model.*
import com.example.qrcodescanner.MainActivity.Companion.REMEMBER_ME
import com.example.qrcodescanner.MainActivity.Companion.rememberMe_sharedPref
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
fun getCurrentUser(): UserData? {

    val gson = Gson()
    val json = MainActivity.selectedUser_sharedPref.getString(SELECTED_USER, null)
    val currentUser = json?.let { gson.fromJson(it, UserData::class.java) }

    return currentUser
}
fun logout(
    shutDownError: MutableState<Boolean>,
    errorMessage: MutableState<String>,
    navController: NavHostController,
) {
    try {
        rememberMe_sharedPref.edit().putString(REMEMBER_ME, null).apply()
        navController.navigate(ScreensRoute.LogInScreen.route) {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }

    } catch (e: Exception) {
        shutDownError.value = true
        errorMessage.value = "Unexpected error: " + e.message.toString()
    }
}

fun getCurrentCamp(): ItemData? {

    val gson = Gson()
    val json = MainActivity.selectedCamp_sharedPref.getString(MainActivity.SELECTED_CAMPP, null)
    val savedCamp = json?.let { gson.fromJson(it, ItemData::class.java) }

    return savedCamp
}

fun getLoginData(): LoginRequirements? {

    val gson = Gson()
    val json = MainActivity.userData_sharedPref.getString(MainActivity.LOGIN_REQUIREMENTS, null)
    val savedData = json?.let { gson.fromJson(it, LoginRequirements::class.java) }

    return savedData
}

fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy")
    var currentDate = sdf.format(Date())

    return currentDate
}

fun getMode(): String? {

    return MainActivity.mode_sharedPref.getString(MainActivity.MODE, null)
}

fun refresh(context: Context) {

    val activity = context as? Activity
    activity?.recreate()
}
fun normalizeText(text: String): String {

    // Convert characters to lowercase and replace multiple consecutive spaces with a single space
    return text.lowercase().replace("\\s+".toRegex(), " ")
}
fun captlization(text: String): String {

    // to convert first character from each name into capital
    return text.split(" ").joinToString(" ") { it.capitalize()}
}
