package com.example.cardify.data

import com.example.cardify.models.BusinessCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CardRepository {
    private val _cards = MutableStateFlow<List<BusinessCard>>(emptyList())
    val cards: StateFlow<List<BusinessCard>> = _cards

    fun addCard(card: BusinessCard) {
        _cards.value = _cards.value + card
    }

    fun updateCard(card: BusinessCard) {
        _cards.value = _cards.value.map { if (it.cardId == card.cardId) card else it }
    }
}
