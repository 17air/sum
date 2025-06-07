package com.example.cardify.models
// (1)cardId (2)name (3)company (4)position (5)phone (6)email (7)sns (8) cardImageUrl

data class BusinessCard(
    val cardid: String,
    val name: String,
    val company: String,
    val position : String,
    val phone: String,
    val email: String,
    val sns: String,
    val imageUrl: String? = null
)

