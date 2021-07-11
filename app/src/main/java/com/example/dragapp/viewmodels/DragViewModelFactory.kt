package com.example.dragapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dragapp.repositories.DragRepository

class DragViewModelFactory (
    private val dragRepository: DragRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DragViewModel(dragRepository) as T
    }

}