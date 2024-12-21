package com.example.bankapp.ui.name_enquiry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bankapp.api.ApiClient
import com.example.bankapp.models.NameEnquiryRequest
import com.example.bankapp.models.NameEnquiryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NameEnquiryViewModel : ViewModel() {

    private val apiService = ApiClient.apiService

    private val _nameEnquiryResponse = MutableLiveData<NameEnquiryResponse?>()
    val nameEnquiryResponse: LiveData<NameEnquiryResponse?> get() = _nameEnquiryResponse

    fun nameEnquiry(accountNumber: String): LiveData<NameEnquiryResponse?> {
        val request = NameEnquiryRequest(accountNumber)
        val token = ApiClient.tokenProvider()?.let { "Bearer $it" } ?: ""
        val call = apiService.nameEnquiry(token, request)
        call.enqueue(object : Callback<NameEnquiryResponse> {
            override fun onResponse(call: Call<NameEnquiryResponse>, response: Response<NameEnquiryResponse>) {
                if (response.isSuccessful) {
                    _nameEnquiryResponse.value = response.body()
                } else {
                    _nameEnquiryResponse.value = null
                }
            }

            override fun onFailure(call: Call<NameEnquiryResponse>, t: Throwable) {
                _nameEnquiryResponse.value = null
            }
        })
        return nameEnquiryResponse
    }
}
