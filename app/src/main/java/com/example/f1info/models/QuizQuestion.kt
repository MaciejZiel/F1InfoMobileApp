package com.example.f1info.models

data class QuizQuestion(
    val question: String,
    val answers: List<String>,
    val correctIndex: Int
)
