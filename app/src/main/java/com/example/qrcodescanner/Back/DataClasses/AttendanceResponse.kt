package com.example.qrcodescanner.Back.DataClasses

data class AttendanceResponse(
    val statusCode: Int,
    val isSuccess: Boolean,
    val message: String,
    val data: TraineeData?,
    val errors: Any?
)
