package com.example.bankapp.models

data class DeleteAccountResponse(
    val responseCode: String,
    val responseMessage: String,
    val accountInfo: Any?,
    val transactionId: Any?
)
