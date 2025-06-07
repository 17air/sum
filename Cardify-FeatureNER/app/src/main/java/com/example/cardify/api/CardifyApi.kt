package com.example.cardify.api
//어떤 요청을 보낼지 설계하는 설계도

import com.example.cardify.requestresponse.CreateCardRequest
import com.example.cardify.requestresponse.CreateCardResponse
import com.example.cardify.requestresponse.EmailCheckRequest
import com.example.cardify.requestresponse.EmailCheckResponse
import com.example.cardify.requestresponse.LoginRequest
import com.example.cardify.requestresponse.LoginResponse
import com.example.cardify.requestresponse.MyCardListResponse
import com.example.cardify.requestresponse.RegisterRequest
import com.example.cardify.requestresponse.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface CardifyApi {
    @POST("api/users/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse
    //suspend : 비동기 네트워크 가능하게 만드는 함수 / LoginRequest를 Body로 하여 request를 보냄 -> LoginResponse의 형태로 DB의 응답을 받음

    @POST("api/users/signup")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse

    @POST("api/users/check-email")
    suspend fun checkEmail(
        @Body request: EmailCheckRequest
    ): EmailCheckResponse

    @GET("api/cards")
    suspend fun getUserCards(
        @Header("Authorization") token: String
    ): List<MyCardListResponse>

    @POST("api/cards")
    suspend fun createCard(
        @Header("Authorization") token: String,
        @Body request: CreateCardRequest
    ): CreateCardResponse
    
    @POST("api/cards/ai")
    suspend fun createCardWithAI(
        @Header("Authorization") token: String,
        @Body request: CreateCardRequest
    ): CreateCardResponse

}
