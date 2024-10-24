package com.example.qrcodescanner.data.model

data class PresentedTraineesResponse(
    val statusCode: Int,
    val isSuccess: Boolean,
    val message: String,
    val data: Data?,
    val errors: Any?
)
