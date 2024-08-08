package com.example.qrcodescanner.back.Model

data class LoginResponse(
    val statusCode: Int,
    val isSuccess: Boolean,
    val message: String,
    val data: UserData?,
    val errors: String?
)
