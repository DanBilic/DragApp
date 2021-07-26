package com.example.dragapp.repositories

import com.example.dragapp.api.RetrofitInstance
import com.example.dragapp.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    suspend fun uploadProfileImage(file: MultipartBody.Part): Response<ImageResult>{
        return RetrofitInstance.api.uploadProfileImage(file)
    }
}