package com.example.qrcodescanner.back.Model

data class AttendanceRegistrationResponse(
    val statusCode: Int,
    val isSuccess: Boolean,
    val message: String,
    val data: TraineeData?,
    val errors: Any?
)
