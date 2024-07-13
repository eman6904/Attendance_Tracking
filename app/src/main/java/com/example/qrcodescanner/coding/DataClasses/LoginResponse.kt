package com.example.qrcodescanner.coding.DataClasses

data class LoginResponse(
    val statusCode: Int,
    val isSuccess: Boolean,
    val message: String,
    val data: UserData?,
    val errors: String?
)
