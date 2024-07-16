package com.example.qrcodescanner.Back.DataClasses

data class PasswordResetRequirements(
    val password:String,
    val token:String,
    val email:String
)
