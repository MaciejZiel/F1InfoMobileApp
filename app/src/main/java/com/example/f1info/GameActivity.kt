package com.example.f1info

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val btnReactionTest = findViewById<Button>(R.id.btnReactionTest)
        btnReactionTest.setOnClickListener {
            startActivity(Intent(this, ReactionTestActivity::class.java))
        }

        val btnDrawTrack = findViewById<Button>(R.id.btnDrawTrack)
        btnDrawTrack.setOnClickListener {
            startActivity(Intent(this, DrawTrackActivity::class.java))
        }

        val btnQuiz = findViewById<Button>(R.id.btnQuiz)
        btnQuiz.setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
        }


    }
}
