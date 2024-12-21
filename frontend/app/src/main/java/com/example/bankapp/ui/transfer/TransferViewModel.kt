package com.example.bankapp.ui.transfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bankapp.api.ApiClient
import com.example.bankapp.models.TransferRequest
import com.example.bankapp.models.TransferResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransferViewModel : ViewModel() {

    private val apiService = ApiClient.apiService

    private val _transferResponse = MutableLiveData<TransferResponse?>()
    val transferResponse: LiveData<TransferResponse?> get() = _transferResponse

    fun transferFunds(sourceAccountNumber: String, destinationAccountNumber: String, amount: String): LiveData<TransferResponse?> {
        val transferRequest = TransferRequest(sourceAccountNumber, destinationAccountNumber, amount)
        val token = ApiClient.tokenProvider()?.let { "Bearer $it" } ?: ""
        val call = apiService.transfer(token, transferRequest)
        call.enqueue(object : Callback<TransferResponse> {
            override fun onResponse(call: Call<TransferResponse>, response: Response<TransferResponse>) {
                if (response.isSuccessful) {
                    _transferResponse.value = response.body()
                } else {
                    _transferResponse.value = null
                }
            }

            override fun onFailure(call: Call<TransferResponse>, t: Throwable) {
                _transferResponse.value = null
            }
        })
        return transferResponse
    }
}
