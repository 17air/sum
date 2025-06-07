package com.example.cardify.models

data class BusinessCardInfo(
    val name: BilingualData = BilingualData(),
    val company: BilingualData = BilingualData(),
    val position: BilingualData = BilingualData(),
    val phone: String = "",
    val email: String = "",
    val sns: String = "",
    val imageUrl: String = "",
    val answers: List<Int> = emptyList(),
    val isBilingual: Boolean = false
) {
    
    // Constructor for backward compatibility
    constructor(
        name: String = "",
        company: String = "",
        position: String = "",
        phone: String = "",
        email: String = "",
        sns: String = "",
        imageUrl: String = "",
        answers: List<Int> = emptyList()
    ) : this(
        name = BilingualData(name),
        company = BilingualData(company),
        position = BilingualData(position),
        phone = phone,
        email = email,
        sns = sns,
        imageUrl = imageUrl,
        answers = answers,
        isBilingual = false
    )
    
    // Convert to single language version
    fun toSingleLanguage(): BusinessCardInfo = copy(
        name = BilingualData(name.toSingleLanguage()),
        company = BilingualData(company.toSingleLanguage()),
        position = BilingualData(position.toSingleLanguage()),
        isBilingual = false
    )
}

