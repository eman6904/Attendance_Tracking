package com.example.qrcodescanner.back.Model

data class ApiResponse(
    val statusCode: Int,
    val isSuccess: Boolean,
    val message: String,
    val data: ArrayList<ItemData> = arrayListOf(),
    val errors: Any?
)
