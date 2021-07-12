package com.example.dragapp.repositories

import com.example.dragapp.api.RetrofitInstance
import com.example.dragapp.models.Login
import com.example.dragapp.models.Register
import com.example.dragapp.models.TokenResult
import com.example.dragapp.models.Result
import retrofit2.Response

class DragRepository {

    suspend fun login(loginData: Login): Response<TokenResult> {
        return RetrofitInstance.api.login(loginData)
    }

    suspend fun register(registerData: Register): Response<TokenResult>{
        return RetrofitInstance.api.register(registerData)
    }

    suspend fun currentUser(): Response<Result>{
        return RetrofitInstance.api.getCurrentUser()
    }
}