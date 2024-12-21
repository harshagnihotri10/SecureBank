package com.example.bankapp.models

data class RejectTransactionResponse(
    val responseCode: String,
    val responseMessage: String,
    val accountInfo: Any?,
    val transactionId: Any?
)


