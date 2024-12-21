package com.example.bankapp.api

import com.example.bankapp.models.ApproveTransactionResponse
import com.example.bankapp.models.BalanceEnquiryRequest
import com.example.bankapp.models.BalanceEnquiryResponse
import com.example.bankapp.models.CreditRequest
import com.example.bankapp.models.CreditResponse
import com.example.bankapp.models.DebitRequest
import com.example.bankapp.models.DebitResponse
import com.example.bankapp.models.DeleteAccountResponse
import com.example.bankapp.models.LoginResponse
import com.example.bankapp.models.NameEnquiryRequest
import com.example.bankapp.models.NameEnquiryResponse
import com.example.bankapp.models.RegisterRequest
import com.example.bankapp.models.RegisterResponse
import com.example.bankapp.models.RejectTransactionResponse
import com.example.bankapp.models.TransferRequest
import com.example.bankapp.models.TransferResponse
import com.example.bankapp.models.UpdateDetailsRequest
import com.example.bankapp.models.UpdateDetailsResponse
import com.example.bankapp.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("/api/user/login")
    fun login(@Body credentials: Map<String, String>): Call<LoginResponse>

    @POST("/api/user")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @GET("/api/user/balanceEnquiry")
    fun balanceEnquiry(@Header("Authorization") token: String, @Body request: BalanceEnquiryRequest): Call<BalanceEnquiryResponse>


    @GET("/api/user/nameEnquiry")
    fun nameEnquiry(@Header("Authorization") token : String, @Body enquiryRequest: NameEnquiryRequest): Call<NameEnquiryResponse>

    @POST("/api/user/credit")
    fun creditAccount(@Header("Authorization") token: String, @Body creditInfo: CreditRequest): Call<CreditResponse>

    @POST("/api/user/debit")
    fun debitAccount(@Header("Authorization") token: String, @Body debitRequest: DebitRequest): Call<DebitResponse>

    @POST("/api/user/transfer")
    fun transfer(@Header("Authorization") token: String, @Body transferInfo: TransferRequest): Call<TransferResponse>

    @GET("/api/user")
    fun getAllUsers(@Header("Authorization") token: String): Call<List<User>>

    @GET("/api/user/{id}")
    fun retrieveUserById(@Header("Authorization") token: String, @Path("id") userId: Int): Call<User>

    @PUT("/api/user/{id}")
    fun updateDetails(@Header("Authorization") token: String, @Path("id") userId: Int, @Body updateDetailsRequest: UpdateDetailsRequest): Call<UpdateDetailsResponse>

    @DELETE("/api/user/{id}")
    fun deleteUser(@Header("Authorization") token: String, @Path("id") userId: Int): Call<DeleteAccountResponse>
//
//    @DELETE("/api/user/{id}")
//    fun deleteUser(@Header("Authorization") token: String, @Path("id") userId: Int): Call<User>

    @POST("/api/transactions/approve/{transactionId}")
    fun approveTransaction(@Header("Authorization") token: String, @Path("transactionId") transactionId: Int): Call<ApproveTransactionResponse>



    @POST("/api/transactions/reject/{transactionId}")
    fun rejectTransaction(@Header("Authorization") token: String, @Path("transactionId") transactionId: Int): Call<RejectTransactionResponse>

}
