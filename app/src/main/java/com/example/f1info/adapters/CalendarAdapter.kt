package com.example.f1info.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.f1info.CircuitDetailsActivity
import com.example.f1info.R
import com.example.f1info.models.CircuitDescription
import com.example.f1info.models.Race
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Suppress("CAST_NEVER_SUCCEEDS")
class CalendarAdapter(private val races: List<Race>) :
    RecyclerView.Adapter<CalendarAdapter.RaceViewHolder>() {

    class RaceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val raceName: TextView = view.findViewById(R.id.tvRaceName)
        val country: TextView = view.findViewById(R.id.tvCountry)
        val date: TextView = view.findViewById(R.id.tvDate)
    }

    private fun getFlagEmoji(country: String): String {
        return when (country.lowercase()) {
            "italy" -> "🇮🇹"
            "uk" -> "🇬🇧"
            "monaco" -> "🇲🇨"
            "belgium" -> "🇧🇪"
            "france" -> "🇫🇷"
            "germany" -> "🇩🇪"
            "netherlands" -> "🇳🇱"
            "spain" -> "🇪🇸"
            "usa" -> "🇺🇸"
            "mexico" -> "🇲🇽"
            "brazil" -> "🇧🇷"
            "japan" -> "🇯🇵"
            "australia" -> "🇦🇺"
            "canada" -> "🇨🇦"
            "china" -> "🇨🇳"
            "austria" -> "🇦🇹"
            "azerbaijan" -> "🇦🇿"
            "qatar" -> "🇶🇦"
            "saudi arabia" -> "🇸🇦"
            "singapore" -> "🇸🇬"
            "south africa" -> "🇿🇦"
            "argentina" -> "🇦🇷"
            "turkey" -> "🇹🇷"
            "portugal" -> "🇵🇹"
            "south korea" -> "🇰🇷"
            "uae" -> "🇦🇪"
            "hungary" -> "🇭🇺"
            "bahrain" -> "🇧🇭"
            else -> ""
        }
    }

    private data class CircuitResIds(
        val main: Int,
        val characteristics: Int,
        val challenges: Int,
        val trivia: Int,
        val drs: Int
    )

    @Suppress("SpellCheckingInspection")
    private fun getCircuitResIds(circuitId: String): CircuitResIds? {
        return when (circuitId.lowercase()) {
            "adelaide" -> CircuitResIds(
                R.string.circuit_adelaide_main,
                R.string.circuit_adelaide_characteristics,
                R.string.circuit_adelaide_challenges,
                R.string.circuit_adelaide_trivia,
                R.string.circuit_adelaide_drs
            )
            "albert_park" -> CircuitResIds(
                R.string.circuit_albert_park_main,
                R.string.circuit_albert_park_characteristics,
                R.string.circuit_albert_park_challenges,
                R.string.circuit_albert_park_trivia,
                R.string.circuit_albert_park_drs
            )
            "americas" -> CircuitResIds(
                R.string.circuit_americas_main,
                R.string.circuit_americas_characteristics,
                R.string.circuit_americas_challenges,
                R.string.circuit_americas_trivia,
                R.string.circuit_americas_drs
            )
            "bahrain" -> CircuitResIds(
                R.string.circuit_bahrain_main,
                R.string.circuit_bahrain_characteristics,
                R.string.circuit_bahrain_challenges,
                R.string.circuit_bahrain_trivia,
                R.string.circuit_bahrain_drs
            )
            "bahrain_outer" -> CircuitResIds(
                R.string.circuit_bahrain_outer_main,
                R.string.circuit_bahrain_outer_characteristics,
                R.string.circuit_bahrain_outer_challenges,
                R.string.circuit_bahrain_outer_trivia,
                R.string.circuit_bahrain_outer_drs
            )
            "baku" -> CircuitResIds(
                R.string.circuit_baku_main,
                R.string.circuit_baku_characteristics,
                R.string.circuit_baku_challenges,
                R.string.circuit_baku_trivia,
                R.string.circuit_baku_drs
            )
            "buddh" -> CircuitResIds(
                R.string.circuit_buddh_main,
                R.string.circuit_buddh_characteristics,
                R.string.circuit_buddh_challenges,
                R.string.circuit_buddh_trivia,
                R.string.circuit_buddh_drs
            )
            "catalunya" -> CircuitResIds(
                R.string.circuit_catalunya_main,
                R.string.circuit_catalunya_characteristics,
                R.string.circuit_catalunya_challenges,
                R.string.circuit_catalunya_trivia,
                R.string.circuit_catalunya_drs
            )
            "fuji" -> CircuitResIds(
                R.string.circuit_fuji_main,
                R.string.circuit_fuji_characteristics,
                R.string.circuit_fuji_challenges,
                R.string.circuit_fuji_trivia,
                R.string.circuit_fuji_drs
            )
            "hanoi" -> CircuitResIds(
                R.string.circuit_hanoi_main,
                R.string.circuit_hanoi_characteristics,
                R.string.circuit_hanoi_challenges,
                R.string.circuit_hanoi_trivia,
                R.string.circuit_hanoi_drs
            )
            "hockenheim" -> CircuitResIds(
                R.string.circuit_hockenheim_main,
                R.string.circuit_hockenheim_characteristics,
                R.string.circuit_hockenheim_challenges,
                R.string.circuit_hockenheim_trivia,
                R.string.circuit_hockenheim_drs
            )
            "hungaroring" -> CircuitResIds(
                R.string.circuit_hungaroring_main,
                R.string.circuit_hungaroring_characteristics,
                R.string.circuit_hungaroring_challenges,
                R.string.circuit_hungaroring_trivia,
                R.string.circuit_hungaroring_drs
            )
            "imola" -> CircuitResIds(
                R.string.circuit_imola_main,
                R.string.circuit_imola_characteristics,
                R.string.circuit_imola_challenges,
                R.string.circuit_imola_trivia,
                R.string.circuit_imola_drs
            )
            "interlagos" -> CircuitResIds(
                R.string.circuit_interlagos_main,
                R.string.circuit_interlagos_characteristics,
                R.string.circuit_interlagos_challenges,
                R.string.circuit_interlagos_trivia,
                R.string.circuit_interlagos_drs
            )
            "istanbul" -> CircuitResIds(
                R.string.circuit_istanbul_main,
                R.string.circuit_istanbul_characteristics,
                R.string.circuit_istanbul_challenges,
                R.string.circuit_istanbul_trivia,
                R.string.circuit_istanbul_drs
            )
            "jeddah" -> CircuitResIds(
                R.string.circuit_jeddah_main,
                R.string.circuit_jeddah_characteristics,
                R.string.circuit_jeddah_challenges,
                R.string.circuit_jeddah_trivia,
                R.string.circuit_jeddah_drs
            )
            "kyalami" -> CircuitResIds(
                R.string.circuit_kyalami_main,
                R.string.circuit_kyalami_characteristics,
                R.string.circuit_kyalami_challenges,
                R.string.circuit_kyalami_trivia,
                R.string.circuit_kyalami_drs
            )
            "losail" -> CircuitResIds(
                R.string.circuit_losail_main,
                R.string.circuit_losail_characteristics,
                R.string.circuit_losail_challenges,
                R.string.circuit_losail_trivia,
                R.string.circuit_losail_drs
            )
            "marina_bay" -> CircuitResIds(
                R.string.circuit_marina_bay_main,
                R.string.circuit_marina_bay_characteristics,
                R.string.circuit_marina_bay_challenges,
                R.string.circuit_marina_bay_trivia,
                R.string.circuit_marina_bay_drs
            )
            "miami" -> CircuitResIds(
                R.string.circuit_miami_main,
                R.string.circuit_miami_characteristics,
                R.string.circuit_miami_challenges,
                R.string.circuit_miami_trivia,
                R.string.circuit_miami_drs
            )
            "monaco" -> CircuitResIds(
                R.string.circuit_monaco_main,
                R.string.circuit_monaco_characteristics,
                R.string.circuit_monaco_challenges,
                R.string.circuit_monaco_trivia,
                R.string.circuit_monaco_drs
            )
            "monza" -> CircuitResIds(
                R.string.circuit_monza_main,
                R.string.circuit_monza_characteristics,
                R.string.circuit_monza_challenges,
                R.string.circuit_monza_trivia,
                R.string.circuit_monza_drs
            )
            "mugello" -> CircuitResIds(
                R.string.circuit_mugello_main,
                R.string.circuit_mugello_characteristics,
                R.string.circuit_mugello_challenges,
                R.string.circuit_mugello_trivia,
                R.string.circuit_mugello_drs
            )
            "nurburgring" -> CircuitResIds(
                R.string.circuit_nurburgring_main,
                R.string.circuit_nurburgring_characteristics,
                R.string.circuit_nurburgring_challenges,
                R.string.circuit_nurburgring_trivia,
                R.string.circuit_nurburgring_drs
            )
            "paul_ricard" -> CircuitResIds(
                R.string.circuit_paul_ricard_main,
                R.string.circuit_paul_ricard_characteristics,
                R.string.circuit_paul_ricard_challenges,
                R.string.circuit_paul_ricard_trivia,
                R.string.circuit_paul_ricard_drs
            )
            "portimao" -> CircuitResIds(
                R.string.circuit_portimao_main,
                R.string.circuit_portimao_characteristics,
                R.string.circuit_portimao_challenges,
                R.string.circuit_portimao_trivia,
                R.string.circuit_portimao_drs
            )
            "red_bull_ring" -> CircuitResIds(
                R.string.circuit_red_bull_ring_main,
                R.string.circuit_red_bull_ring_characteristics,
                R.string.circuit_red_bull_ring_challenges,
                R.string.circuit_red_bull_ring_trivia,
                R.string.circuit_red_bull_ring_drs
            )
            "sepang" -> CircuitResIds(
                R.string.circuit_sepang_main,
                R.string.circuit_sepang_characteristics,
                R.string.circuit_sepang_challenges,
                R.string.circuit_sepang_trivia,
                R.string.circuit_sepang_drs
            )
            "shanghai" -> CircuitResIds(
                R.string.circuit_shanghai_main,
                R.string.circuit_shanghai_characteristics,
                R.string.circuit_shanghai_challenges,
                R.string.circuit_shanghai_trivia,
                R.string.circuit_shanghai_drs
            )
            "silverstone" -> CircuitResIds(
                R.string.circuit_silverstone_main,
                R.string.circuit_silverstone_characteristics,
                R.string.circuit_silverstone_challenges,
                R.string.circuit_silverstone_trivia,
                R.string.circuit_silverstone_drs
            )
            "sochi" -> CircuitResIds(
                R.string.circuit_sochi_main,
                R.string.circuit_sochi_characteristics,
                R.string.circuit_sochi_challenges,
                R.string.circuit_sochi_trivia,
                R.string.circuit_sochi_drs
            )
            "spa" -> CircuitResIds(
                R.string.circuit_spa_main,
                R.string.circuit_spa_characteristics,
                R.string.circuit_spa_challenges,
                R.string.circuit_spa_trivia,
                R.string.circuit_spa_drs
            )
            "suzuka" -> CircuitResIds(
                R.string.circuit_suzuka_main,
                R.string.circuit_suzuka_characteristics,
                R.string.circuit_suzuka_challenges,
                R.string.circuit_suzuka_trivia,
                R.string.circuit_suzuka_drs
            )
            "valencia" -> CircuitResIds(
                R.string.circuit_valencia_main,
                R.string.circuit_valencia_characteristics,
                R.string.circuit_valencia_challenges,
                R.string.circuit_valencia_trivia,
                R.string.circuit_valencia_drs
            )
            "vegas" -> CircuitResIds(
                R.string.circuit_vegas_main,
                R.string.circuit_vegas_characteristics,
                R.string.circuit_vegas_challenges,
                R.string.circuit_vegas_trivia,
                R.string.circuit_vegas_drs
            )
            "villeneuve" -> CircuitResIds(
                R.string.circuit_villeneuve_main,
                R.string.circuit_villeneuve_characteristics,
                R.string.circuit_villeneuve_challenges,
                R.string.circuit_villeneuve_trivia,
                R.string.circuit_villeneuve_drs
            )
            "yas_marina" -> CircuitResIds(
                R.string.circuit_yas_marina_main,
                R.string.circuit_yas_marina_characteristics,
                R.string.circuit_yas_marina_challenges,
                R.string.circuit_yas_marina_trivia,
                R.string.circuit_yas_marina_drs
            )
            "yeongam" -> CircuitResIds(
                R.string.circuit_yeongam_main,
                R.string.circuit_yeongam_characteristics,
                R.string.circuit_yeongam_challenges,
                R.string.circuit_yeongam_trivia,
                R.string.circuit_yeongam_drs
            )
            "zandvoort" -> CircuitResIds(
                R.string.circuit_zandvoort_main,
                R.string.circuit_zandvoort_characteristics,
                R.string.circuit_zandvoort_challenges,
                R.string.circuit_zandvoort_trivia,
                R.string.circuit_zandvoort_drs
            )
            else -> null
        }
    }

    private fun getCircuitDescription(context: Context, circuitId: String): CircuitDescription {
        val resIds = getCircuitResIds(circuitId)
            ?: return CircuitDescription(
                main = context.getString(R.string.circuit_no_description),
                characteristics = "-",
                challenges = "-",
                trivia = "-",
                drs = "-"
            )

        return CircuitDescription(
            main = context.getString(resIds.main),
            characteristics = context.getString(resIds.characteristics),
            challenges = context.getString(resIds.challenges),
            trivia = context.getString(resIds.trivia),
            drs = context.getString(resIds.drs)
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_race, parent, false)
        return RaceViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RaceViewHolder, position: Int) {
        val race = races[position]
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, CircuitDetailsActivity::class.java).apply {
                putExtra("circuit_name", race.sessionName)
                putExtra("country", race.country)
                putExtra("race_date", race.date)
                putExtra(
                    "description",
                    getCircuitDescription(context, race.circuitId) as java.io.Serializable
                )
                putExtra("meeting_key", race.meetingKey)
                putExtra("circuit_id", race.circuitId)
            }
            context.startActivity(intent)
        }
        holder.raceName.text = race.sessionName
        holder.country.text = "${getFlagEmoji(race.country)} ${race.country}"
        holder.date.text = formatDate(race.date)
    }

    override fun getItemCount() = races.size
}

fun formatDate(dateString: String): String {
    return try {
        val zdt = ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val instant = zdt.toInstant()
        val date = Date.from(instant)
        val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        formatter.format(date)
    } catch (_: Exception) {
        dateString
    }
}
