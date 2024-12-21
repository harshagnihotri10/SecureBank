package com.example.bankapp.models

data class DebitResponse(
    val responseCode: String,
    val responseMessage: String,
    val accountInfo: AccountInfo?,
    val transactionId: String?
)

data class AccountInfo(
    val accountName: String,
    val accountBalance: Double,
    val accountNumber: String
)
