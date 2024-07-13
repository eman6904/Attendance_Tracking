package com.example.qrcodescanner


import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.qrcodescanner.MainActivity.Companion.SELECTED_CAMPP
import com.example.qrcodescanner.MainActivity.Companion.SELECTED_DATE
import com.example.qrcodescanner.MainActivity.Companion.selectedCamp_sharedPref
import com.example.qrcodescanner.MainActivity.Companion.selectedDate_sharedPref
import com.example.qrcodescanner.coding.APIs.ViewModel
import com.example.qrcodescanner.coding.functions.getCurrentDate
import com.example.qrcodescanner.coding.functions.getCurrentUser
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : ComponentActivity() {

    companion object {

        val SELECTED_CAMPP = "Locale.Helper.Selected.Camp"
        val SELECTED_USER = "Locale.Helper.Selected.User"
        val SELECTED_DATE = "Locale.Helper.Selected.Date"
        // Initialize SharedPreferences
        lateinit var selectedCamp_sharedPref: SharedPreferences
        lateinit var selectedUser_sharedPref: SharedPreferences
        lateinit var selectedDate_sharedPref: SharedPreferences
        val viewModel = ViewModel()
        lateinit var token:String
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       setContent {
           selectedCamp_sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
           selectedUser_sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
           selectedDate_sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

           updateDate()
           val navController= rememberNavController()
           appNavGraph(navController = navController)
          // login2(navController = navController)
     }
 }
}
fun updateDate(){

    val newDate=getCurrentDate()
    val oldDate=selectedDate_sharedPref.getString(SELECTED_DATE,null)
    if(oldDate!=newDate) {
        Log.d("oldDate",oldDate.toString())
        Log.d("newDate",newDate)
        selectedDate_sharedPref.edit().putString(SELECTED_DATE, newDate).apply()
        selectedCamp_sharedPref.edit().putString(SELECTED_CAMPP,null).apply()
    }

}
