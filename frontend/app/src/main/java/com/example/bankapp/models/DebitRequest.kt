package com.example.bankapp.models

data class DebitRequest(
    val accountNumber: String,
    val amount: String
)
