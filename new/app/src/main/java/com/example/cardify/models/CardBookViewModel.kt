package com.example.cardify.models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CardBookViewModel : ViewModel() {
    private val _cards = MutableStateFlow<List<BusinessCard>>(emptyList())
    val cards: StateFlow<List<BusinessCard>> = _cards

    fun addCard(card: BusinessCard) {
        _cards.value = _cards.value + card
    }

    fun updateCard(updated: BusinessCard) {
        _cards.value = _cards.value.map {
            if (it.cardId == updated.cardId) updated else it
        }
    }
}

