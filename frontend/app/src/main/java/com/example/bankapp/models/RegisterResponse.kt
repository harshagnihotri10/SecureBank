package com.example.bankapp.models


data class RegisterResponse(
    val responseCode: String,
    val responseMessage: String,
    val userId: Int?
)
