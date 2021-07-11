package com.example.dragapp.repositories

import com.example.dragapp.api.RetrofitInstance
import com.example.dragapp.models.Login
import com.example.dragapp.models.Register
import com.example.dragapp.models.TokenResult
import retrofit2.Response

class DragRepository {

    suspend fun login(loginData: Login): Response<TokenResult> {
        return RetrofitInstance.api.login(loginData)
    }

    suspend fun register(registerData: Register): Response<TokenResult>{
        return RetrofitInstance.api.register(registerData)
    }
}