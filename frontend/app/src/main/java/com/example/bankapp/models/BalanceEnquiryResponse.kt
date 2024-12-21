package com.example.bankapp.models

data class BalanceEnquiryResponse(
    val responseCode: String,
    val responseMessage: String,
    val accountInfo: AccountInfo?,
    val transactionId: String?
)


