package com.example.f1info

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.f1info.adapters.LapAdapter
import com.example.f1info.models.CircuitStats
import com.example.f1info.models.Lap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL

class CircuitDetailsActivity : AppCompatActivity() {

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circuit_details)
        val tvLapHeader = findViewById<TextView>(R.id.tvLapHeader)
        val tvCircuitName = findViewById<TextView>(R.id.tvCircuitName)
        val tvRaceDate = findViewById<TextView>(R.id.tvRaceDate)
        val tvCountry = findViewById<TextView>(R.id.tvCountry)
        val ivCircuitImage = findViewById<ImageView>(R.id.ivCircuitImage)
        val tvCircuitInfo = findViewById<TextView>(R.id.tvCircuitInfo)

        val circuitName = intent.getStringExtra("circuit_name") ?: "Unknown"
        val raceDate = intent.getStringExtra("race_date") ?: "-"
        val country = intent.getStringExtra("country") ?: "-"
//        val circuitImageUrl = intent.getStringExtra("image_url") ?: ""
        val circuitImageUrl = "https://upload.wikimedia.org/wikipedia/commons/2/2f/Circuit_Monza_2000.png"
        Log.d("CircuitDebug", "Image URL: $circuitImageUrl")


        val description = intent.getStringExtra("description") ?: "Brak opisu."

        tvCircuitName.text = circuitName
        tvRaceDate.text = raceDate
        tvCountry.text = country
        tvCircuitInfo.text = description

//        Glide.with(this)
//            .load(circuitImageUrl)
//            .into(ivCircuitImage)
//        ivCircuitImage.setImageResource(R.drawable.cover)

        val circuitId = intent.getStringExtra("circuit_id") ?: ""
        val imageRes = when (circuitId.lowercase()) {
            "shanghai" -> R.drawable.china_circuit
            "albert_park" -> R.drawable.australia_circuit
            "bahrain" -> R.drawable.bahrain_circuit
            "catalunya" -> R.drawable.spain_circuit
            "hungaroring" -> R.drawable.hungary_circuit
            "monaco" -> R.drawable.monaco_circuit
            "baku" -> R.drawable.baku_circuit
            "villeneuve" -> R.drawable.canada_circuit
            "silverstone" -> R.drawable.great_britain_circuit
            "red_bull_ring" -> R.drawable.austria_circuit
            "paul_ricard" -> R.drawable.france_circuit
            "spa" -> R.drawable.belgium_circuit
            "zandvoort" -> R.drawable.netherlands_circuit
            "monza" -> R.drawable.italy_circuit
            "marina_bay" -> R.drawable.singapore_circuit
            "suzuka" -> R.drawable.japan_circuit
            "americas" -> R.drawable.usa_circuit
            "rodriguez" -> R.drawable.mexico_circuit
            "interlagos" -> R.drawable.brazil_circuit
            "yas_marina" -> R.drawable.abu_dhabi_circuit
            "jeddah" -> R.drawable.saudi_arabia_circuit
            "miami" -> R.drawable.miami_circuit
            "losail" -> R.drawable.qatar_circuit
            "vegas" -> R.drawable.las_vegas_circuit
            "imola" -> R.drawable.emilia_romagna_circuit
            else -> R.drawable.test
        }
        Log.d("CircuitDebug", "circuitId = $circuitId")


        ivCircuitImage.setImageResource(imageRes)



        val recyclerViewLaps = findViewById<RecyclerView>(R.id.recyclerViewLaps)
        recyclerViewLaps.layoutManager = LinearLayoutManager(this)

        val testLaps = listOf(
            Lap("44", "1:23.456"),
            Lap("1", "1:23.789"),
            Lap("16", "1:24.012")
        )

        recyclerViewLaps.adapter = LapAdapter(testLaps)
        recyclerViewLaps.visibility = View.VISIBLE

        findViewById<TextView>(R.id.tvLapHeader).visibility = View.VISIBLE


        // 🔽 TU WKLEJ:
        val meetingKey = intent.getIntExtra("meeting_key", -1)
        val stats = when (circuitId.lowercase()) {
            "baku" -> CircuitStats(
                firstGrandPrix = "2016",
                lapCount = "51",
                circuitLength = "6.003 km",
                raceDistance = "306.049 km",
                lapRecord = "1:43.009 (Charles Leclerc, 2019)"
            )
            else -> CircuitStats()
        }

        findViewById<TextView>(R.id.tvFirstGrandPrix).text = stats.firstGrandPrix
        findViewById<TextView>(R.id.tvLapCount).text = stats.lapCount



        CoroutineScope(Dispatchers.IO).launch {
            try {
                val lapResponse = URL("https://api.openf1.org/v1/lap_times?meeting_key=$meetingKey").readText()
                val lapsJson = JSONArray(lapResponse)

                val allLaps = mutableListOf<Lap>()
                for (i in 0 until lapsJson.length()) {
                    val lap = lapsJson.getJSONObject(i)
                    val driverNumber = lap.optString("driver_number", "-")
                    val lapTime = lap.optString("lap_time", "-")
                    if (lapTime != "-") {
                        allLaps.add(Lap(driverNumber, lapTime))
                    }
                }

                val sorted = allLaps.sortedBy { it.lapTime }.take(10)

                withContext(Dispatchers.Main) {
                    if (sorted.isNotEmpty()) {
                        recyclerViewLaps.adapter = LapAdapter(sorted)
                        recyclerViewLaps.visibility = View.VISIBLE
                        tvLapHeader.visibility = View.VISIBLE
                    }
                }


            } catch (e: Exception) {
                Log.e("LapError", "Nie udało się pobrać okrążeń", e)
            }
        }

    }
}


