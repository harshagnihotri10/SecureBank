package com.example.bankapp.ui.approve_transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bankapp.api.ApiClient
import com.example.bankapp.models.ApproveTransactionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApproveTransactionViewModel : ViewModel() {

    var token: String? = null

    fun approveTransaction(transactionId: Int): LiveData<ApproveTransactionResponse?> {
        val liveData = MutableLiveData<ApproveTransactionResponse?>()
        val call = ApiClient.apiService.approveTransaction("Bearer $token", transactionId)
        call.enqueue(object : Callback<ApproveTransactionResponse> {
            override fun onResponse(call: Call<ApproveTransactionResponse>, response: Response<ApproveTransactionResponse>) {
                if (response.isSuccessful) {
                    liveData.value = response.body()
                } else {
                    liveData.value = null
                }
            }

            override fun onFailure(call: Call<ApproveTransactionResponse>, t: Throwable) {
                liveData.value = null
            }
        })
        return liveData
    }
}
