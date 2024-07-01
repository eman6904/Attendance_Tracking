package com.example.qrcodescanner.coding.DataClasses

data class ApiResponse(
    val statusCode: Int,
    val isSuccess: Boolean,
    val message: String,
    val data: ArrayList<Details> = arrayListOf(),
    val errors: Any?
)
