package com.example.qrcodescanner.back.Model

data class PasswordResetResponse(
    val statusCode: Int,
    val isSuccess: Boolean,
    val message: String,
    val data: String,
    val errors: Map<String, List<String>>
)