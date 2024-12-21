package com.example.bankapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bankapp.api.ApiClient
import com.example.bankapp.api.ApiService
import com.example.bankapp.models.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val apiService: ApiService = ApiClient.apiService

    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?> get() = _loginResponse

    fun login(email: String, password: String): LiveData<LoginResponse?> {
        val credentials = mapOf("email" to email, "password" to password)
        val call = apiService.login(credentials)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    _loginResponse.value = response.body()
                } else {
                    _loginResponse.value = null // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _loginResponse.value = null // Handle failure
            }
        })
        return loginResponse
    }
}
