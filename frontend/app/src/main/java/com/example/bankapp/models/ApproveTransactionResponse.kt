package com.example.bankapp.models

data class ApproveTransactionResponse(
    val responseCode: String,
    val responseMessage: String,
    val accountInfo: Any?,
    val transactionId: Any?
)
