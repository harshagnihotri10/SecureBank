//package com.example.bankapp.models
//data class User(
//    val id: Int,
//    val name: String,
//    val email: String,
//    val balance: Double,
//    val accountNumber: String
//)
package com.example.bankapp.models

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val otherName: String?,
    val gender: String,
    val address: String,
    val stateOfOrigin: String,
    val accountNumber: String,
    val accountBalance: Double,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val alternatePhoneNumber: String?,
    val status: String,
    val role: String,
    val createdAt: String,
    val modifiedAt: String,
    val transactionLimit: Double?,
    val requiresApproval: Boolean,
    val enabled: Boolean,
    val approved: Boolean,
    val username: String,
    val authorities: List<Authority>,
    val accountNonExpired: Boolean,
    val accountNonLocked: Boolean,
    val credentialsNonExpired: Boolean
)

data class Authority(
    val authority: String
)
