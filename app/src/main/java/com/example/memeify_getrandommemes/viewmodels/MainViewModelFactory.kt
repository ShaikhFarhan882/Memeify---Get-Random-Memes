package com.example.memeify_getrandommemes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.memeify_getrandommemes.repository.MemeRepository

class MainViewModelFactory(private val memeRepository: MemeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(memeRepository) as T
    }
}