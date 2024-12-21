package com.example.bankapp.ui.reject_transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bankapp.api.ApiClient
import com.example.bankapp.models.RejectTransactionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RejectTransactionViewModel : ViewModel() {

    private val _rejectTransactionResponse = MutableLiveData<RejectTransactionResponse?>()
    val rejectTransactionResponse: LiveData<RejectTransactionResponse?> get() = _rejectTransactionResponse

    fun rejectTransaction(transactionId: Int): LiveData<RejectTransactionResponse?> {
        ApiClient.apiService.rejectTransaction("Bearer ${ApiClient.tokenProvider.invoke()}", transactionId).enqueue(object : Callback<RejectTransactionResponse> {
            override fun onResponse(call: Call<RejectTransactionResponse>, response: Response<RejectTransactionResponse>) {
                if (response.isSuccessful) {
                    _rejectTransactionResponse.value = response.body()
                } else {
                    _rejectTransactionResponse.value = null
                }
            }

            override fun onFailure(call: Call<RejectTransactionResponse>, t: Throwable) {
                _rejectTransactionResponse.value = null
            }
        })
        return rejectTransactionResponse
    }
}
