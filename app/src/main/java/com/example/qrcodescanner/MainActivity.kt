package com.example.qrcodescanner


import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.example.qrcodescanner.coding.APIs.ViewModel
import com.example.qrcodescanner.design.*


class MainActivity : ComponentActivity() {

    companion object {

        val SELECTED_CAMP = "Locale.Helper.Selected.Camp"
        // Initialize SharedPreferences
        lateinit var sharedPreferences: SharedPreferences
        val viewModel = ViewModel()
        val token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJod" +
                "HRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2" +
                "lkZW50aXR5L2NsYWltcy9uYW1laWRlbnRpZmllciI6ImEyNT" +
                "RlOTAwLTIzODQtNGVlZS05ZmI3LWUwZjc0MWIxZTdkMiIsImh" +
                "0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDU" +
                "vaWRlbnRpdHkvY2xhaW1zL25hbWUiOiJJQ1BDU29oYWciLCJ" +
                "odHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1" +
                "L2lkZW50aXR5L2NsYWltcy9lbWFpbGFkZHJlc3MiOiJpY3BjLn" +
                "NvaGFnLmNvbW11bml0eUBnbWFpbC5jb20iLCJodHRwOi8vc2NoZ" +
                "W1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHk" +
                "vY2xhaW1zL3JvbGUiOiJMZWFkZXIiLCJleHA" +
                "iOjE3MjAyMDA4MDIsImlzcyI6IklDUEMiLCJhdWQiOiJm" +
                "cm9udGVuZCJ9.BLfReOJonLvG9T16dXqoIZTN_G1W9ssmPJHc8W1kD3w"

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
