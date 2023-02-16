package com.example.memeify_getrandommemes.api

import com.example.memeify_getrandommemes.const.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitBuilder {
    companion object{
        private val retrofit by lazy{
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val API by lazy {
            retrofit.create(MemeService::class.java)
        }
    }
}