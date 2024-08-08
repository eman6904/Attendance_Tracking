package com.example.qrcodescanner


import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.example.qrcodescanner.back.API.ApiConnection
import com.example.qrcodescanner.back.function.getCurrentDate
import com.example.qrcodescanner.back.function.getMode
import com.example.qrcodescanner.navigation.appNavGraph
import com.example.qrcodescanner.front.theme.ICPCTheme


class MainActivity : ComponentActivity() {

    companion object {

        val SELECTED_CAMPP = "Locale.Helper.Selected.Camp"
        val SELECTED_USER = "Locale.Helper.Selected.User"
        val SELECTED_DATE = "Locale.Helper.Selected.Date"
        val REMEMBER_ME = "Locale.Helper.Selected.Remember"
        val LOGIN_REQUIREMENTS = "Locale.Helper.Selected.Login"
        val MODE = "Locale.Helper.Selected.Mode"

        // Initialize SharedPreferences
        lateinit var selectedCamp_sharedPref: SharedPreferences
        lateinit var selectedUser_sharedPref: SharedPreferences
        lateinit var selectedDate_sharedPref: SharedPreferences
        lateinit var rememberMe_sharedPref: SharedPreferences
        lateinit var userData_sharedPref: SharedPreferences
        lateinit var mode_sharedPref: SharedPreferences
        val connect = ApiConnection()
        lateinit var token: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        selectedCamp_sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        selectedUser_sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        selectedDate_sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        rememberMe_sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        userData_sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        mode_sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        setContent {

            val darkTheme = remember { mutableStateOf(false) }
            if (getMode() == null) {
                darkTheme.value = isSystemInDarkTheme()
                if (darkTheme.value) {
                    mode_sharedPref.edit().putString(MODE, "Light Mode").apply()
                } else {
                    mode_sharedPref.edit().putString(MODE, "Dark Mode").apply()
                }
            } else {
                when (getMode()) {
                    "Dark Mode" -> {
                        darkTheme.value = false
                    }
                    "Light Mode" -> {
                        darkTheme.value = true
                    }
                }
            }
            ICPCTheme(darkTheme = darkTheme.value) {
                updateDate()
                val navController = rememberNavController()
                appNavGraph(navController = navController)

            }
        }
    }

    fun updateDate() {

        val newDate = getCurrentDate()
        val oldDate = selectedDate_sharedPref.getString(SELECTED_DATE, null)
        if (oldDate != newDate) {
//        Log.d("oldDate",oldDate.toString())
//        Log.d("newDate",newDate)
            selectedDate_sharedPref.edit().putString(SELECTED_DATE, newDate).apply()
            selectedCamp_sharedPref.edit().putString(SELECTED_CAMPP, null).apply()
        }

    }
}
