package com.example.bankapp.models

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val otherName: String = "",
    val gender: String,
    val address: String,
    val stateOfOrigin: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val alternatePhoneNumber: String = "",
    val transactionLimit: Double,
    val requiresApproval: Boolean
)
