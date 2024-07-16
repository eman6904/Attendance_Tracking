package com.example.qrcodescanner.Back.APIs


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConnection {

    fun connect ():ApiServices {
        val apiServices: ApiServices
        val retrofit = Retrofit.Builder()
            .baseUrl("https://icpc.runasp.net/api/")
            .addConverterFactory(GsonConverterFactory.create()).build()
         apiServices = retrofit.create(ApiServices::class.java)
        return apiServices
    }

}
