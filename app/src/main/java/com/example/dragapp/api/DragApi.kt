package com.example.dragapp.api

import com.example.dragapp.models.Login
import com.example.dragapp.models.TokenResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DragApi {
    @POST("api/v1/auth/login")
    suspend fun login(@Body login: Login): Response<TokenResult>
}