package com.example.memeify_getrandommemes.api

import com.example.memeify_getrandommemes.model.MemeResponse
import retrofit2.Response
import retrofit2.http.GET

/*https://meme-api.com/gimme*/

interface MemeService {
    @GET("gimme")
    suspend fun getRandomMeme() : Response<MemeResponse>
}