package com.example.bankapp.models

data class UpdateDetailsResponse(
    val responseCode: String,
    val responseMessage: String,
    val accountInfo: AccountInfo?,
    val transactionId: String?
)
