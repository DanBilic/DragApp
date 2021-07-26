package com.example.dragapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dragapp.models.*
import com.example.dragapp.repositories.DragRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class DragViewModel (private val repository: DragRepository): ViewModel(){
    val loginData : MutableLiveData<Response<TokenResult>> = MutableLiveData()
    val registerData : MutableLiveData<Response<TokenResult>> = MutableLiveData()
    val currentUserData : MutableLiveData<Response<Result>> = MutableLiveData()
    val currentProfileImageData : MutableLiveData<Response<ImageResult>> = MutableLiveData()

    fun login(loginInput: Login){
        viewModelScope.launch {
            loginData.value = repository.login(loginInput)
        }
    }

    fun register(registerInput: Register){
        viewModelScope.launch {
            registerData.value = repository.register((registerInput))
        }
    }

    fun currentUser(){
        viewModelScope.launch {
            currentUserData.value = repository.currentUser()
        }
    }

    fun uploadProfileImage(file: MultipartBody.Part){
        viewModelScope.launch {
            currentProfileImageData.value = repository.uploadProfileImage(file)
        }
    }
}