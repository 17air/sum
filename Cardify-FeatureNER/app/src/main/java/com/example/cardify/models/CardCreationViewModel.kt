// Create a file: app/src/main/java/com/example/cardify/viewmodel/CardCreationViewModel.kt
package com.example.cardify.models

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardify.api.RetrofitInstance
import com.example.cardify.requestresponse.BilingualTextData
import com.example.cardify.requestresponse.CreateCardRequest
import com.example.cardify.requestresponse.CreateCardResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class CardCreationState {
    object Loading : CardCreationState()
    data class Success(val response: CreateCardResponse) : CardCreationState()
    data class Error(val message: String) : CardCreationState()
}

class CardCreationViewModel : ViewModel() {
    private val _cardCreationState = mutableStateOf<CardCreationState>(CardCreationState.Loading)
    val cardCreationState: State<CardCreationState> = _cardCreationState
    
    private val _token = mutableStateOf("")
    val token: State<String> = _token

    private val _creationResult = MutableStateFlow<Result<CreateCardResponse>?>(null)
    val creationResult: StateFlow<Result<CreateCardResponse>?> = _creationResult
    private val _cardInfo = MutableStateFlow(BusinessCardInfo())
    val cardInfo: StateFlow<BusinessCardInfo> = _cardInfo.asStateFlow()
    private val _currentQuestion = MutableStateFlow(1)
    val currentQuestion: StateFlow<Int> = _currentQuestion.asStateFlow()
    private val _selectedCardId = MutableStateFlow<String>("")
    val selectedCardId: StateFlow<String> = _selectedCardId
    private val _answers = MutableStateFlow<List<Int>>(listOf())
    val answers: StateFlow<List<Int>> = _answers


    fun createCardWithAI(cardInfo: BusinessCardInfo, userAnswers: List<Int>) {
        viewModelScope.launch {
            try {
                // Prepare AI request with card info and user answers
                val aiRequest = CreateCardRequest(
                    name = BilingualTextData(
                        korean = cardInfo.name.korean,
                        english = cardInfo.name.english,
                        isBilingual = cardInfo.isBilingual
                    ),
                    company = BilingualTextData(
                        korean = cardInfo.company.korean,
                        english = cardInfo.company.english,
                        isBilingual = cardInfo.isBilingual
                    ),
                    position = BilingualTextData(
                        korean = cardInfo.position.korean,
                        english = cardInfo.position.english,
                        isBilingual = cardInfo.isBilingual
                    ),
                    phone = cardInfo.phone,
                    email = cardInfo.email,
                    sns = cardInfo.sns,
                    selectedCardId = "ai_design", // Special ID for AI design
                    answers = userAnswers
                )
                
                // Call AI API
                try {
                    val response = RetrofitInstance.api.createCardWithAI(
                        token = token.value,
                        request = aiRequest
                    )
                    
                    _cardCreationState.value = CardCreationState.Success(response)
                } catch (e: Exception) {
                    _cardCreationState.value = CardCreationState.Error("AI 명함 생성에 실패했습니다")
                }
            } catch (e: Exception) {
                _cardCreationState.value = CardCreationState.Error(e.message ?: "AI 명함 생성 중 오류가 발생했습니다")
            }
        }
    }

    fun createCard(token: String) {
        viewModelScope.launch {
            try {
                _cardCreationState.value = CardCreationState.Loading
                
                val request = CreateCardRequest(
                    name = BilingualTextData(
                        korean = cardInfo.value.name.korean,
                        english = cardInfo.value.name.english,
                        isBilingual = cardInfo.value.isBilingual
                    ),
                    company = BilingualTextData(
                        korean = cardInfo.value.company.korean,
                        english = cardInfo.value.company.english,
                        isBilingual = cardInfo.value.isBilingual
                    ),
                    position = BilingualTextData(
                        korean = cardInfo.value.position.korean,
                        english = cardInfo.value.position.english,
                        isBilingual = cardInfo.value.isBilingual
                    ),
                    phone = cardInfo.value.phone,
                    email = cardInfo.value.email,
                    sns = cardInfo.value.sns,
                    selectedCardId = this@CardCreationViewModel.selectedCardId.value,
                    answers = answers.value
                )
                val bearerToken = "Bearer $token"
                val response = RetrofitInstance.api.createCard(bearerToken, request)
                _creationResult.value = Result.success(response)
            } catch (e: Exception) {
                _creationResult.value = Result.failure(e)
            }
        }
    }

    fun clearResult() {
        _creationResult.value = null
    }

    fun resetCreation() {
        _cardInfo.value = BusinessCardInfo()
        _currentQuestion.value = 1
        _answers.value = listOf()
        _selectedCardId.value = ""
    }

    fun updateCardInfo(info: BusinessCardInfo) {
        _cardInfo.value = info
    }

    fun recordAnswer(questionNumber: Int, answer: Int) {
        val newAnswers = _answers.value.toMutableList()
        
        // Ensure the list has enough elements
        while (newAnswers.size <= questionNumber - 1) {
            newAnswers.add(0)
        }

        newAnswers[questionNumber - 1] = answer
        _answers.value = newAnswers
        
        // Move to next question
        _currentQuestion.value = _currentQuestion.value + 1
    }

    fun selectCard(cardId: String) {
        _selectedCardId.value = cardId
    }
}