package com.example.qrcodescanner.coding.DataClasses

data class PasswordResetRequirements(
    val password:String,
    val token:String,
    val email:String
)
