package com.example.bankapp.ui.credit_account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bankapp.api.ApiClient
import com.example.bankapp.models.CreditRequest
import com.example.bankapp.models.CreditResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreditAccountViewModel : ViewModel() {

    private val apiService = ApiClient.apiService

    private val _creditResponse = MutableLiveData<CreditResponse?>()
    val creditResponse: LiveData<CreditResponse?> get() = _creditResponse

    fun creditAccount(accountNumber: String, amount: String): LiveData<CreditResponse?> {
        val creditRequest = CreditRequest(accountNumber, amount)
        val call = apiService.creditAccount("Bearer ${ApiClient.tokenProvider()}", creditRequest)
        call.enqueue(object : Callback<CreditResponse> {
            override fun onResponse(call: Call<CreditResponse>, response: Response<CreditResponse>) {
                if (response.isSuccessful) {
                    _creditResponse.value = response.body()
                } else {
                    _creditResponse.value = null // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<CreditResponse>, t: Throwable) {
                _creditResponse.value = null // Handle failure
            }
        })
        return creditResponse
    }
}
