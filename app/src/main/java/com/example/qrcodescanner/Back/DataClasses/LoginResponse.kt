package com.example.qrcodescanner.Back.DataClasses

data class LoginResponse(
    val statusCode: Int,
    val isSuccess: Boolean,
    val message: String,
    val data: UserData?,
    val errors: String?
)
