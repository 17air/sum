package com.example.cardify.models

data class BilingualData(
    val korean: String = "",
    val english: String = "",
    val isBilingual: Boolean = false
) {
    
    // Constructor for backward compatibility
    constructor(text: String) : this(
        korean = text,
        english = text,
        isBilingual = false
    )
    
    // Convert to single language if not bilingual
    fun toSingleLanguage(): String = if (isBilingual) korean else english
    
    // Convert from single language
    companion object {
        fun fromSingleLanguage(text: String) = BilingualData(text)
    }
}
