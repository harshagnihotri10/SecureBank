package com.example.bankapp.ui.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bankapp.api.ApiService
import com.example.bankapp.models.RegisterRequest
import com.example.bankapp.models.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterViewModel : ViewModel() {

    // Initialize Retrofit and ApiService
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.2:8080")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    private val _registerResponse = MutableLiveData<RegisterResponse?>()
    val registerResponse: LiveData<RegisterResponse?> get() = _registerResponse

    fun register(registerRequest: RegisterRequest): LiveData<RegisterResponse?> {
        val call = apiService.register(registerRequest)
        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    _registerResponse.value = response.body()
                } else {
                    _registerResponse.value = null // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _registerResponse.value = null // Handle failure
            }
        })
        return registerResponse
    }
}
