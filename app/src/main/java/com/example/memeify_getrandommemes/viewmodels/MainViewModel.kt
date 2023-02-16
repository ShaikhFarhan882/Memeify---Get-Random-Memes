package com.example.memeify_getrandommemes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.memeify_getrandommemes.model.MemeResponse
import com.example.memeify_getrandommemes.repository.MemeRepository
import kotlinx.coroutines.launch

class MainViewModel(private val memeRepository: MemeRepository) : ViewModel() {

    private val _memeData: MutableLiveData<MemeResponse> = MutableLiveData()
    val memeData: LiveData<MemeResponse> get() = _memeData


    fun getMeme() = viewModelScope.launch {
        val response = memeRepository.getRandomMeme()
        if (response.isSuccessful) {
            response.body()?.let {
                _memeData.postValue(it)
            }
        }
    }

    init {
        getMeme()
    }


}