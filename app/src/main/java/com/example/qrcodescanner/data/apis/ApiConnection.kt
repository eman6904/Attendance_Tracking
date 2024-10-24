package com.example.qrcodescanner.data.apis


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ApiConnection @Inject constructor(){

    fun connect ():ApiServices {
        val apiServices: ApiServices
        val retrofit = Retrofit.Builder()
            .baseUrl("https://icpc.runasp.net/api/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        apiServices = retrofit.create(ApiServices::class.java)
        return apiServices
    }

}
//https://icpc.sitecom.top/api/