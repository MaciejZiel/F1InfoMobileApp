package com.example.f1info

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.f1info.models.QuizQuestion

class QuizActivity : AppCompatActivity() {

    private lateinit var tvQuestion: TextView
    private lateinit var btnAnswer1: Button
    private lateinit var btnAnswer2: Button
    private lateinit var btnAnswer3: Button
    private lateinit var tvQuizResult: TextView
    private lateinit var btnRetryQuiz: Button

    private val questions = listOf(
        QuizQuestion(
            "Który kierowca zdobył najwięcej tytułów mistrza świata?",
            listOf("Sebastian Vettel", "Michael Schumacher", "Lewis Hamilton"),
            2
        ),
        QuizQuestion(
            "Który zespół ma najwięcej tytułów konstruktorów?",
            listOf("Mercedes", "Ferrari", "McLaren"),
            1
        ),
        QuizQuestion(
            "Ile Grand Prix liczy sezon 2024?",
            listOf("20", "22", "24"),
            2
        )
    )

    private var currentQuestionIndex = 0
    private var score = 0
    private var answerSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        tvQuestion = findViewById(R.id.tvQuestion)
        btnAnswer1 = findViewById(R.id.btnAnswer1)
        btnAnswer2 = findViewById(R.id.btnAnswer2)
        btnAnswer3 = findViewById(R.id.btnAnswer3)
        tvQuizResult = findViewById(R.id.tvQuizResult)
        btnRetryQuiz = findViewById(R.id.btnRetryQuiz)
        loadQuestion()
        val answerButtons = listOf(btnAnswer1, btnAnswer2, btnAnswer3)
        answerButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                if (answerSelected) return@setOnClickListener
                answerSelected = true
                val correctIndex = questions[currentQuestionIndex].correctIndex
                if (index == correctIndex) {
                    button.setBackgroundColor(getColor(android.R.color.holo_green_light))
                    score++
                } else {
                    button.setBackgroundColor(getColor(android.R.color.holo_red_light))
                    answerButtons[correctIndex].setBackgroundColor(getColor(android.R.color.holo_green_light))
                }
                if (currentQuestionIndex + 1 < questions.size) {
                    currentQuestionIndex++
                    loadQuestion()
                } else {
                    showFinalResult()
                }
            }
        }
        btnRetryQuiz.setOnClickListener {
            currentQuestionIndex = 0
            score = 0
            tvQuizResult.visibility = TextView.GONE
            btnRetryQuiz.visibility = Button.GONE
            loadQuestion()
        }
    }

    private fun loadQuestion() {
        val question = questions[currentQuestionIndex]
        tvQuestion.text = question.question
        btnAnswer1.text = question.answers[0]
        btnAnswer2.text = question.answers[1]
        btnAnswer3.text = question.answers[2]

        btnAnswer1.setBackgroundColor(getColor(android.R.color.transparent))
        btnAnswer2.setBackgroundColor(getColor(android.R.color.transparent))
        btnAnswer3.setBackgroundColor(getColor(android.R.color.transparent))
        tvQuizResult.visibility = TextView.GONE
        btnRetryQuiz.visibility = Button.GONE
        answerSelected = false
    }

    @SuppressLint("SetTextI18n")
    private fun showFinalResult() {
        tvQuizResult.text = "Twój wynik: $score/${questions.size}"
        tvQuizResult.visibility = TextView.VISIBLE
        btnRetryQuiz.visibility = Button.VISIBLE
    }
}
