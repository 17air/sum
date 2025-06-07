// com.example.cardify.api.RetrofitInstance.kt

package com.example.cardify.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: CardifyApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://your.api.server/") //여기 서버 주소가 들어가야 해
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CardifyApi::class.java)
    }
}
