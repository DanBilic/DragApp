package com.example.dragapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dragapp.models.Login
import com.example.dragapp.models.Register
import com.example.dragapp.models.TokenResult
import com.example.dragapp.repositories.DragRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class DragViewModel (private val repository: DragRepository): ViewModel(){
    val loginData : MutableLiveData<Response<TokenResult>> = MutableLiveData()
    val registerData : MutableLiveData<Response<TokenResult>> = MutableLiveData()

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
}