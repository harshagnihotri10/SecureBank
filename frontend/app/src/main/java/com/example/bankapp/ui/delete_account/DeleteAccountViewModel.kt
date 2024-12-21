package com.example.bankapp.ui.delete_account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bankapp.api.ApiClient
import com.example.bankapp.models.DeleteAccountResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteAccountViewModel : ViewModel() {

    private val _deleteAccountResponse = MutableLiveData<DeleteAccountResponse?>()
    val deleteAccountResponse: LiveData<DeleteAccountResponse?> get() = _deleteAccountResponse

    fun deleteAccount(userId: Int): LiveData<DeleteAccountResponse?> {
        val token = ApiClient.tokenProvider()
        if (token != null) {
            val call = ApiClient.apiService.deleteUser("Bearer $token", userId)
            call.enqueue(object : Callback<DeleteAccountResponse> {
                override fun onResponse(
                    call: Call<DeleteAccountResponse>,
                    response: Response<DeleteAccountResponse>
                ) {
                    if (response.isSuccessful) {
                        _deleteAccountResponse.value = response.body()
                    } else {
                        _deleteAccountResponse.value = null // Handle unsuccessful response
                    }
                }

                override fun onFailure(call: Call<DeleteAccountResponse>, t: Throwable) {
                    _deleteAccountResponse.value = null // Handle failure
                }
            })
        }
        return deleteAccountResponse
    }
}
