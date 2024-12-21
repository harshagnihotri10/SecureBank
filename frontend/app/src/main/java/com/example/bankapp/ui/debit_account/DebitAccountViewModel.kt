package com.example.bankapp.ui.debit_account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bankapp.api.ApiClient
import com.example.bankapp.models.DebitRequest
import com.example.bankapp.models.DebitResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DebitAccountViewModel : ViewModel() {

    private val apiService = ApiClient.apiService

    private val _debitResponse = MutableLiveData<DebitResponse?>()
    val debitResponse: LiveData<DebitResponse?> get() = _debitResponse

    fun debitAccount(accountNumber: String, amount: String): LiveData<DebitResponse?> {
        val debitRequest = DebitRequest(accountNumber, amount)
        val token = ApiClient.tokenProvider()?.let { "Bearer $it" } ?: ""
        val call = apiService.debitAccount(token, debitRequest)
        call.enqueue(object : Callback<DebitResponse> {
            override fun onResponse(call: Call<DebitResponse>, response: Response<DebitResponse>) {
                if (response.isSuccessful) {
                    _debitResponse.value = response.body()
                } else {
                    _debitResponse.value = null
                }
            }

            override fun onFailure(call: Call<DebitResponse>, t: Throwable) {
                _debitResponse.value = null
            }
        })
        return debitResponse
    }
}
