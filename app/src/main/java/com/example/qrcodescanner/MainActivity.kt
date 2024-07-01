package com.example.qrcodescanner


import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.qrcodescanner.design.*


class MainActivity : ComponentActivity() {

    companion object {

        val SELECTED_CAMP = "Locale.Helper.Selected.Camp"
        // Initialize SharedPreferences
        lateinit var sharedPreferences: SharedPreferences

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       setContent {
           sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
           val navController= rememberNavController()
           appNavGraph(navController = navController)
          // login2(navController = navController)
     }
}
}
