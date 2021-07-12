package com.example.dragapp.api

import com.example.dragapp.utils.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// object cuz RetrofitInstance is singleton
// object -> the kotlin way to create singletons
object RetrofitInstance {

    // add custom interceptor
    private val client = OkHttpClient.Builder().apply {
        addInterceptor(RetrofitInterceptor())
    }.build()

    private val retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val api: DragApi by lazy {
        retrofit.create(DragApi::class.java)
    }
}