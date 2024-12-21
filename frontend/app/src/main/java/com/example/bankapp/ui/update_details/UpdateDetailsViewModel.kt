package com.example.bankapp.ui.update_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bankapp.api.ApiClient
import com.example.bankapp.models.UpdateDetailsRequest
import com.example.bankapp.models.UpdateDetailsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateDetailsViewModel : ViewModel() {

    private val _updateDetailsResponse = MutableLiveData<UpdateDetailsResponse?>()
    val updateDetailsResponse: LiveData<UpdateDetailsResponse?> get() = _updateDetailsResponse

    fun updateDetails(userId: Int, updateDetailsRequest: UpdateDetailsRequest): LiveData<UpdateDetailsResponse?> {
        val token = ApiClient.tokenProvider() ?: ""
        val apiService = ApiClient.apiService
        val call = ApiClient.apiService.updateDetails("Bearer $token", userId, updateDetailsRequest)

        call.enqueue(object : Callback<UpdateDetailsResponse> {
            override fun onResponse(call: Call<UpdateDetailsResponse>, response: Response<UpdateDetailsResponse>) {
                if (response.isSuccessful) {
                    _updateDetailsResponse.value = response.body()
                } else {
                    _updateDetailsResponse.value = null // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<UpdateDetailsResponse>, t: Throwable) {
                _updateDetailsResponse.value = null // Handle failure
            }
        })

        return updateDetailsResponse
    }
}
