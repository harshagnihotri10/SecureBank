package com.example.bankapp.ui.balance_enquiry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bankapp.api.ApiClient
import com.example.bankapp.api.ApiService
import com.example.bankapp.models.BalanceEnquiryRequest
import com.example.bankapp.models.BalanceEnquiryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BalanceEnquiryViewModel : ViewModel() {

    private val apiService: ApiService = ApiClient.apiService

    private val _balanceEnquiryResponse = MutableLiveData<BalanceEnquiryResponse?>()
    val balanceEnquiryResponse: LiveData<BalanceEnquiryResponse?> get() = _balanceEnquiryResponse

    fun getBalanceEnquiry(accountNumber: String): LiveData<BalanceEnquiryResponse?> {
        val request = BalanceEnquiryRequest(accountNumber)
        val call = apiService.balanceEnquiry(ApiClient.tokenProvider.invoke() ?: "", request)
        call.enqueue(object : Callback<BalanceEnquiryResponse> {
            override fun onResponse(call: Call<BalanceEnquiryResponse>, response: Response<BalanceEnquiryResponse>) {
                if (response.isSuccessful) {
                    _balanceEnquiryResponse.value = response.body()
                } else {
                    _balanceEnquiryResponse.value = null
                }
            }

            override fun onFailure(call: Call<BalanceEnquiryResponse>, t: Throwable) {
                _balanceEnquiryResponse.value = null
            }
        })
        return balanceEnquiryResponse
    }
}
