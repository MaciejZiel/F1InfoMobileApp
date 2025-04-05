package com.example.f1info

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import android.widget.ImageView

class DrawTrackActivity : AppCompatActivity() {
    private lateinit var drawView: DrawTrackView
    private lateinit var btnClear: Button
    private lateinit var btnSimulate: Button
    private lateinit var carIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_track)
        carIcon = findViewById(R.id.carIcon)
        drawView = findViewById(R.id.drawView)
        btnClear = findViewById(R.id.btnClear)
        btnSimulate = findViewById(R.id.btnSimulate)
        btnClear.setOnClickListener {
            drawView.clear()
        }

        btnSimulate.setOnClickListener {
            if (drawView.trackPoints.size < 10 || !drawView.isTrackClosed()) {
                Toast.makeText(this, "Tor musi być dłuższy i zamknięty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            startSimulation()
        }
    }

    private fun startSimulation() {
        val points = drawView.trackPoints
        if (points.size < 2) return
        carIcon.visibility = ImageView.VISIBLE
        carIcon.x = points[0].x - carIcon.width / 2
        carIcon.y = points[0].y - carIcon.height / 2
        val animator = ValueAnimator.ofInt(0, points.size - 1)
        animator.duration = 4000L // 4 sekundy jazdy
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { valueAnimator ->
            val index = valueAnimator.animatedValue as Int
            if (index < points.size) {
                val point = points[index]
                carIcon.x = point.x - carIcon.width / 2
                carIcon.y = point.y - carIcon.height / 2
            }
        }
        animator.start()
    }
}
