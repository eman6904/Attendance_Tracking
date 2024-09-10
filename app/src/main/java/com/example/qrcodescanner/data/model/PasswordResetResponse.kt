package com.example.qrcodescanner.data.model

data class PasswordResetResponse(
    val statusCode: Int,
    val isSuccess: Boolean,
    val message: String,
    val data: String,
    val errors: Map<String, List<String>>
)