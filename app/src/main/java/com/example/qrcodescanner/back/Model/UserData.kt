package com.example.qrcodescanner.back.Model

data class UserData(
    val id: String,
    val firstName: String,
    val middleName: String,
    val roles: List<String>,
    val token: String,
    val photoUrl: String?
)
