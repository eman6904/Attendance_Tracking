package com.example.qrcodescanner.coding.DataClasses

data class ForgetPasswordResponse(
    val statusCode: Int,
    val isSuccess: Boolean,
    val message: String,
    val data: String,
    val errors: Map<String, List<String>>
)