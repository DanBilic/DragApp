package com.example.dragapp.api

import okhttp3.Interceptor
import okhttp3.Response

class RetrofitInterceptor: Interceptor {
    companion object {
      var authToken: String = "none"


        fun setRequestHeaderToken(token: String){
            authToken = token
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
            .newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("X-Platform", "Android")
            .addHeader("X-Auth-Token", "123456789")
            .addHeader("Authorization", authToken)
            .build()

        return chain.proceed(request)
    }

}