package com.example.bankapp.ui.retrieve_user_by_id

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bankapp.api.ApiClient
import com.example.bankapp.models.User
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class RetrieveUserByIdViewModel : ViewModel() {

    private val apiService = ApiClient.apiService
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    fun retrieveUserById(userId: Int): LiveData<User?> {
        val token = ApiClient.tokenProvider()?.let { "Bearer $it" } ?: ""

        val call = apiService.retrieveUserById(token, userId)

        call.enqueue(object : retrofit2.Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    _user.value = response.body()
                } else {
                    _user.value = null
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                _user.value = null
            }
        })

        return user
    }
}