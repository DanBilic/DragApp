package com.example.dragapp.api

import com.example.dragapp.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface DragApi {
    @POST("api/v1/auth/login")
    suspend fun login(@Body login: Login): Response<TokenResult>

    @POST("api/v1/auth/register")
    suspend fun register(@Body register: Register): Response<TokenResult>

    @GET("api/v1/auth/current-user")
    suspend fun getCurrentUser(): Response<Result>

    @Multipart
    @PUT("api/v1/auth/image")
    suspend fun uploadProfileImage(
        @Part part: MultipartBody.Part,
    ): Response<ImageResult>
}