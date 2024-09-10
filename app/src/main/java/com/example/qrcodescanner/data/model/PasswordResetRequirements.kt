package com.example.qrcodescanner.data.model

data class PasswordResetRequirements(
    val password:String,
    val token:String,
    val email:String
)
