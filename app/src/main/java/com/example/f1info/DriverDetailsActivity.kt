package com.example.f1info

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DriverDetailsActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_details)
        val name = intent.getStringExtra("driver_name")
        val team = intent.getStringExtra("team_name")
        val points = intent.getIntExtra("points", 0)
        val imageUrl = intent.getStringExtra("image_url")

        val currentSeasonPodiums = listOf(1, 5, 3, 7, 2, 10)
        currentSeasonPodiums.count { it in 1..3 }

        findViewById<TextView>(R.id.tvDriverName).text = "$name\n$team - $points pts"
        val imageView = findViewById<ImageView>(R.id.ivDriverImage)
        Glide.with(this).load(imageUrl).into(imageView)

        val cardPodiums = findViewById<View>(R.id.cardPodiums)
        cardPodiums.findViewById<TextView>(R.id.tvStatTitle).text = "Total Podiums"

        val cardChampionship = findViewById<View>(R.id.cardChampionship)
        cardChampionship.findViewById<TextView>(R.id.tvStatTitle).text = "Championship Standing"
        cardChampionship.findViewById<TextView>(R.id.tvStatValue).text = "2"

        val cardPoints = findViewById<View>(R.id.cardPoints)
        cardPoints.findViewById<TextView>(R.id.tvStatTitle).text = "Points"
        cardPoints.findViewById<TextView>(R.id.tvStatValue).text = points.toString()

    }
}
