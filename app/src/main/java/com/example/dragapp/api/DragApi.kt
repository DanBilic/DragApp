package com.example.dragapp.api

import com.example.dragapp.models.Login
import com.example.dragapp.models.Register
import com.example.dragapp.models.TokenResult
import com.example.dragapp.models.Result
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DragApi {
    @POST("api/v1/auth/login")
    suspend fun login(@Body login: Login): Response<TokenResult>

    @POST("api/v1/auth/register")
    suspend fun register(@Body register: Register): Response<TokenResult>

    @GET("api/v1/auth/current-user")
    suspend fun getCurrentUser(): Response<Result>
}