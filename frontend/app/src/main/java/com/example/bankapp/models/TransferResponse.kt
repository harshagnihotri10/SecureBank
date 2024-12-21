package com.example.bankapp.models

data class TransferResponse(
    val responseCode: String,
    val responseMessage: String,
    val accountInfo: AccountInfo?,
    val transactionId: String?
)
