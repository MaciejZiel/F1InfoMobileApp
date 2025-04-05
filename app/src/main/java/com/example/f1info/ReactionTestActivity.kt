package com.example.f1info

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class ReactionTestActivity : AppCompatActivity() {

    private lateinit var rootLayout: RelativeLayout
    private lateinit var tvStatus: TextView
    private lateinit var tvResult: TextView
    private lateinit var btnRetry: Button

    private var testFinished = false
    private val handler = Handler(Looper.getMainLooper())
    private var startTime: Long = 0L
    private var canClick = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reaction_test)

        rootLayout = findViewById(R.id.reactionRoot)
        tvStatus = findViewById(R.id.tvStatus)
        tvResult = findViewById(R.id.tvResult)
        btnRetry = findViewById(R.id.btnRetry)

        startTest()

        rootLayout.setOnClickListener {
            if (testFinished) return@setOnClickListener

            if (canClick) {
                val reactionTime = (System.nanoTime() - startTime) / 1_000_000 // ms
                tvStatus.text = "Twój czas: $reactionTime ms"
                tvResult.text = ""
                rootLayout.setBackgroundColor(Color.DKGRAY)
                canClick = false
                testFinished = true
                btnRetry.visibility = View.VISIBLE
            } else {
                tvStatus.text = "FALSTART 🚫"
                tvResult.text = "Kliknięcie przed sygnałem"
                tvResult.setTextColor(Color.WHITE)
                rootLayout.setBackgroundColor(Color.DKGRAY)
                canClick = false
                testFinished = true
                handler.removeCallbacksAndMessages(null)
                btnRetry.visibility = View.VISIBLE
            }
        }

        btnRetry.setOnClickListener {
            startTest()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun startTest() {
        tvStatus.text = "Przygotuj się..."
        tvResult.text = ""
        tvResult.setTextColor(Color.WHITE)
        rootLayout.setBackgroundColor(Color.RED)
        canClick = false
        testFinished = false
        btnRetry.visibility = View.GONE

        val delayMillis = Random.nextLong(3000, 7000)

        handler.postDelayed({
            tvStatus.text = "KLIKNIJ!"
            rootLayout.setBackgroundColor(Color.GREEN)
            startTime = System.nanoTime()
            canClick = true
        }, delayMillis)
    }
}
