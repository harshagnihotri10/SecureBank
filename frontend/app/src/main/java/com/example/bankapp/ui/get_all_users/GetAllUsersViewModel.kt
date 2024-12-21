package com.example.bankapp.ui.get_all_users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bankapp.api.ApiClient
import com.example.bankapp.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetAllUsersViewModel : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    fun fetchAllUsers(token: String) {
        ApiClient.apiService.getAllUsers(token).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    _users.value = response.body()
                } else {
                    _users.value = emptyList()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _users.value = emptyList()
            }
        })
    }
}
