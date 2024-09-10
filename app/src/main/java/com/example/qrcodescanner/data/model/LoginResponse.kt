package com.example.qrcodescanner.data.model

data class LoginResponse(
    val statusCode: Int,
    val isSuccess: Boolean,
    val message: String,
    val data: UserData?,
    val errors: String?
)
