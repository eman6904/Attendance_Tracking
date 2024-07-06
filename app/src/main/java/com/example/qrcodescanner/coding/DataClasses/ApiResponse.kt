package com.example.qrcodescanner.coding.DataClasses

data class ApiResponse(
    val statusCode: Int,
    val isSuccess: Boolean,
    val message: String,
    val data: ArrayList<ItemDetails> = arrayListOf(),
    val errors: Any?
)
