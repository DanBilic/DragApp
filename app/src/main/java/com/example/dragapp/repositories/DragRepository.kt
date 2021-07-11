package com.example.dragapp.repositories

import com.example.dragapp.api.RetrofitInstance
import com.example.dragapp.models.Login
import com.example.dragapp.models.TokenResult
import retrofit2.Response

class DragRepository {

    suspend fun login(): Response<TokenResult> {
        val loginData = Login("dan@gmail.com", "123456")
        return RetrofitInstance.api.login(loginData)
    }
}