package com.example.bankapp
import android.app.Application
import com.example.bankapp.api.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication : Application() {

    lateinit var retrofit: Retrofit
        private set

    lateinit var apiService: ApiService
        private set

    override fun onCreate() {
        super.onCreate()

        retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.2:8080")
//        ("http://10.12.99.218:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
    }
}
