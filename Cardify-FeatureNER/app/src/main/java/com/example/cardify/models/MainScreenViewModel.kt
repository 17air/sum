package com.example.cardify.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardify.api.RetrofitInstance
import com.example.cardify.requestresponse.MyCardListResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel : ViewModel() {
    // StateFlow : 카드 목록 상태를 저장
    private val _cards = MutableStateFlow<List<MyCardListResponse>>(emptyList())
    val cards: StateFlow<List<MyCardListResponse>> = _cards

    // StateFlow : 오류 메시지 저저장
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchCards(token: String) {
        viewModelScope.launch {
            try {
                // "Bearer <token>" 형식으로 헤더 작성
                val bearerToken = "Bearer $token"
                // Retrofit을 사용하여 API 호출
                val result = RetrofitInstance.api.getUserCards(bearerToken)
                // 성공 시 카드 리스트 업데이트
                _cards.value = result
            } catch (e: Exception) {
                if (token == "dummy-token") {
                    _cards.value = emptyList()
                    _error.value = null
                } else {
                    _error.value = e.message
                }
            }
        }
    }


    // 옵션? 상태 초기화 함수
    fun clear() {
        _cards.value = emptyList()
        _error.value = null
    }
}