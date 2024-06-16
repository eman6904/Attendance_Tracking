package com.example.qrcodescanner


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.qrcodescanner.design.*


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       setContent {

           val navController= rememberNavController()
           appNavGraph(navController = navController)
          // login2(navController = navController)
     }
}
}
