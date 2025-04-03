package com.example.f1info

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.f1info.models.CircuitStats
import com.example.f1info.models.Lap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL

class CircuitDetailsActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circuit_details)

        val tvCircuitName = findViewById<TextView>(R.id.tvCircuitName)
        val tvRaceDate = findViewById<TextView>(R.id.tvRaceDate)
        val tvCountry = findViewById<TextView>(R.id.tvCountry)
        val ivCircuitImage = findViewById<ImageView>(R.id.ivCircuitImage)
        val tvCircuitInfo = findViewById<TextView>(R.id.tvCircuitInfo)
        val tvCircuitLength = findViewById<TextView>(R.id.tvCircuitLength)
        val tvRaceDistance = findViewById<TextView>(R.id.tvRaceDistance)
        val tvLapRecord = findViewById<TextView>(R.id.tvLapRecord)
        val tvFirstGrandPrix = findViewById<TextView>(R.id.tvFirstGrandPrix)
        val tvLapCount = findViewById<TextView>(R.id.tvLapCount)

        val circuitName = intent.getStringExtra("circuit_name") ?: "Unknown"
        val raceDate = intent.getStringExtra("race_date") ?: "-"
        val country = intent.getStringExtra("country") ?: "-"
        val description = intent.getStringExtra("description") ?: "Brak opisu."
        val circuitId = intent.getStringExtra("circuit_id") ?: ""

        tvCircuitName.text = circuitName
        tvRaceDate.text = raceDate
        tvCountry.text = country
        tvCircuitInfo.text = description

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

        ivCircuitImage.setImageResource(imageRes)

        val stats = getCircuitStats(circuitId)
        tvCircuitLength.text = stats.circuitLength
        tvRaceDistance.text = stats.raceDistance
        tvLapRecord.text = stats.lapRecord
        tvFirstGrandPrix.text = stats.firstGrandPrix
        tvLapCount.text = stats.lapCount
    }






    private fun getCircuitStats(circuitId: String): CircuitStats {
        return when (circuitId.lowercase()) {
            "suzuka" -> CircuitStats("1987", "53", "5.807 km", "307.471 km", "1:30.983 (Lewis Hamilton, 2019)")
            "albert_park" -> CircuitStats("1996", "58", "5.278 km", "306.124 km", "1:20.235 (Charles Leclerc, 2022)")
            "monza" -> CircuitStats("1950", "53", "5.793 km", "306.72 km", "1:21.046 (Rubens Barrichello, 2004)")
            "spa" -> CircuitStats("1950", "44", "7.004 km", "308.052 km", "1:46.286 (Valtteri Bottas, 2018)")
            "bahrain" -> CircuitStats("2004", "57", "5.412 km", "308.238 km", "1:31.447 (Pedro de la Rosa, 2005)")
            "silverstone" -> CircuitStats("1950", "52", "5.891 km", "306.198 km", "1:27.097 (Max Verstappen, 2020)")
            "hungaroring" -> CircuitStats("1986", "70", "4.381 km", "306.63 km", "1:16.627 (Lewis Hamilton, 2020)")
            "americas" -> CircuitStats("2012", "56", "5.513 km", "308.405 km", "1:36.169 (Charles Leclerc, 2019)")
            "yas_marina" -> CircuitStats("2009", "58", "5.281 km", "306.183 km", "1:26.103 (Max Verstappen, 2021)")
            "interlagos" -> CircuitStats("1973", "71", "4.309 km", "305.879 km", "1:10.540 (Valtteri Bottas, 2018)")
            "rodriguez" -> CircuitStats("1963", "71", "4.304 km", "305.354 km", "1:17.774 (Valtteri Bottas, 2021)")
            "villeneuve" -> CircuitStats("1978", "70", "4.361 km", "305.27 km", "1:13.078 (Valtteri Bottas, 2019)")
            "zandvoort" -> CircuitStats("1952", "72", "4.259 km", "306.648 km", "1:11.097 (Lewis Hamilton, 2021)")
            "baku" -> CircuitStats("2016", "51", "6.003 km", "306.049 km", "1:43.009 (Charles Leclerc, 2019)")
            "miami" -> CircuitStats("2022", "57", "5.412 km", "308.326 km", "1:29.708 (Max Verstappen, 2023)")
            "jeddah" -> CircuitStats("2021", "50", "6.174 km", "308.45 km", "1:30.734 (Lewis Hamilton, 2021)")
            "losail" -> CircuitStats("2021", "57", "5.419 km", "308.611 km", "1:24.319 (Max Verstappen, 2021)")
            "vegas" -> CircuitStats("2023", "50", "6.201 km", "310.05 km", "1:35.490 (Oscar Piastri, 2023)")
            "marina_bay" -> CircuitStats("2008", "62", "4.928 km", "306.143 km", "1:35.867 (Lewis Hamilton, 2023)")
            "shanghai" -> CircuitStats("2004", "56", "5.451 km", "305.066 km", "1:32.238 (Michael Schumacher, 2004)")
            "paul_ricard" -> CircuitStats("1971", "53", "5.842 km", "309.69 km", "1:32.740 (Sebastian Vettel, 2019)")
            "red_bull_ring" -> CircuitStats("1970", "71", "4.318 km", "306.452 km", "1:05.619 (Carlos Sainz, 2020)")
            "catalunya" -> CircuitStats("1991", "66", "4.657 km", "307.236 km", "1:16.330 (Max Verstappen, 2023)")
            "imola" -> CircuitStats("1980", "63", "4.909 km", "309.049 km", "1:15.484 (Lewis Hamilton, 2020)")
            else -> CircuitStats()
        }
    }
}
