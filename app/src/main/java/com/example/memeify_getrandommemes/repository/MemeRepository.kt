package com.example.memeify_getrandommemes.repository

import com.example.memeify_getrandommemes.api.RetrofitBuilder

class MemeRepository {

    suspend fun getRandomMeme() = RetrofitBuilder.API.getRandomMeme()

}