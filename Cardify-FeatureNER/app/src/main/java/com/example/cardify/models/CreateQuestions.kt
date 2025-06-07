package com.example.cardify.models

data class Question(
    val id: Int,
    val question: String,
    val options: List<String>
)

object QuestionBank {
    val questions = listOf(
        Question(
            id = 1,
            question = "당신의 성격을 가장 잘 표현하는 단어는?",
            options = listOf(
                "열정적",
                "정직한",
                "창의적인",
                "정확한"
            )
        ),
        Question(
            id = 2,
            question = "당신이 가장 중요하게 생각하는 가치는?",
            options = listOf(
                "성공",
                "성장",
                "공동체",
                "안정"
            )
        ),
        Question(
            id = 3,
            question = "당신의 업무 스타일은?",
            options = listOf(
                "분석적",
                "직관적",
                "팀워크",
                "효율적"
            )
        ),
        Question(
            id = 4,
            question = "당신의 리더십 스타일은?",
            options = listOf(
                "민주적",
                "독단적",
                "전략적",
                "지도적"
            )
        ),
        Question(
            id = 5,
            question = "당신의 업무 시간 관리는?",
            options = listOf(
                "계획적",
                "즉흥적",
                "효율적",
                "자유로운"
            )
        ),
        Question(
            id = 6,
            question = "당신의 의사소통 방식은?",
            options = listOf(
                "직접적",
                "간접적",
                "상세한",
                "간단한"
            )
        ),
        Question(
            id = 7,
            question = "당신의 스트레스 관리는?",
            options = listOf(
                "운동",
                "명상",
                "여행",
                "독서"
            )
        ),
        Question(
            id = 8,
            question = "당신의 성장 동기는?",
            options = listOf(
                "도전",
                "경험",
                "발전",
                "성취"
            )
        ),
        Question(
            id = 9,
            question = "당신의 휴식 방식은?",
            options = listOf(
                "활동적",
                "정적",
                "여유롭게",
                "집중적으로"
            )
        ),
        Question(
            id = 10,
            question = "당신의 업무 효율성은?",
            options = listOf(
                "빠른",
                "정확한",
                "창의적인",
                "계획적"
            )
        )
    )
}
