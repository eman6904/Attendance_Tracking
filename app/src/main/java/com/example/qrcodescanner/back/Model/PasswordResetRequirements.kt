package com.example.qrcodescanner.back.Model

data class PasswordResetRequirements(
    val password:String,
    val token:String,
    val email:String
)
