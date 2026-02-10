package com.example.f1info

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.f1info.api.JolpicaApiService
import com.example.f1info.api.JolpicaClient
import com.example.f1info.api.OpenF1ApiService
import com.example.f1info.api.OpenF1Client
import retrofit2.HttpException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DriverDetailsActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_details)
        val name = intent.getStringExtra("driver_name")
        val team = intent.getStringExtra("team_name")
        val points = intent.getDoubleExtra("points", 0.0)
        val imageUrl = intent.getStringExtra("image_url")
        val driverId = intent.getStringExtra("driver_id")
        val driverNumber = intent.getIntExtra("driver_number", -1).takeIf { it > 0 }
        val championshipPosition = intent.getIntExtra("championship_position", 0)

        val pointsText = if (points % 1.0 == 0.0) points.toInt().toString() else points.toString()
        findViewById<TextView>(R.id.tvDriverName).text = "$name\n$team - $pointsText pts"
        val imageView = findViewById<ImageView>(R.id.ivDriverImage)
        Glide.with(this).load(imageUrl).into(imageView)

        val cardPodiums = findViewById<View>(R.id.cardPodiums)
        cardPodiums.findViewById<TextView>(R.id.tvStatTitle).text = "Season Podiums"
        val tvPodiumsValue = cardPodiums.findViewById<TextView>(R.id.tvStatValue)
        tvPodiumsValue.text = "-"

        val cardChampionship = findViewById<View>(R.id.cardChampionship)
        cardChampionship.findViewById<TextView>(R.id.tvStatTitle).text = "Championship Standing"
        val champValue = if (championshipPosition > 0) championshipPosition.toString() else "-"
        cardChampionship.findViewById<TextView>(R.id.tvStatValue).text = champValue

        val cardPoints = findViewById<View>(R.id.cardPoints)
        cardPoints.findViewById<TextView>(R.id.tvStatTitle).text = "Points"
        cardPoints.findViewById<TextView>(R.id.tvStatValue).text = pointsText

        val season = getSelectedSeason()
        lifecycleScope.launch {
            val cacheKey = "podiums_${season}_${driverId ?: driverNumber ?: "unknown"}"
            val cachePrefs = getSharedPreferences("F1_STATS", MODE_PRIVATE)
            val cached = cachePrefs.getInt(cacheKey, -1)
            if (cached >= 0) {
                tvPodiumsValue.text = cached.toString()
                return@launch
            }

            try {
                val podiums = if (season >= 2023 && driverNumber != null) {
                    fetchOpenF1SeasonPodiums(season, driverNumber)
                } else if (driverId != null) {
                    fetchErgastSeasonPodiums(season, driverId)
                } else {
                    null
                }

                if (podiums != null) {
                    cachePrefs.edit().putInt(cacheKey, podiums).apply()
                    tvPodiumsValue.text = podiums.toString()
                }
            } catch (e: HttpException) {
                tvPodiumsValue.text = "-"
            } catch (e: Exception) {
                tvPodiumsValue.text = "-"
            }
        }
    }

    private fun getSelectedSeason(): Int {
        val prefs = getSharedPreferences("F1_PREFS", MODE_PRIVATE)
        val selected = prefs.getString("selected_season", "2026") ?: "2026"
        return selected.toIntOrNull() ?: 2026
    }

    private suspend fun fetchOpenF1SeasonPodiums(season: Int, driverNumber: Int): Int {
        return withContext(Dispatchers.IO) {
            val api = OpenF1Client.getInstance().create(OpenF1ApiService::class.java)
            val sessions = api.getSessions(year = season, sessionType = "Race")
            var count = 0
            for (session in sessions) {
                val results = api.getSessionResult(session.session_key)
                val result = results.firstOrNull { it.driver_number == driverNumber }
                if (result != null && result.position in 1..3) {
                    count++
                }
            }
            count
        }
    }

    private suspend fun fetchErgastSeasonPodiums(season: Int, driverId: String): Int {
        return withContext(Dispatchers.IO) {
            val api = JolpicaClient.getInstance().create(JolpicaApiService::class.java)
            val response = api.getDriverResults(season.toString(), driverId, 500)
            val races = response.MRData.RaceTable.Races
            var count = 0
            for (race in races) {
                val result = race.Results.firstOrNull()
                val pos = result?.position?.toIntOrNull() ?: 0
                if (pos in 1..3) {
                    count++
                }
            }
            count
        }
    }
}
