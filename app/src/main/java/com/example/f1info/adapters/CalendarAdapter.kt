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

    private fun getCircuitDescription(context: Context, circuitId: String): CircuitDescription {
        val base = when (circuitId.lowercase()) {
            "albert_park" -> "circuit_albert_park"
            "bahrain" -> "circuit_bahrain"
            "catalunya" -> "circuit_catalunya"
            "monaco" -> "circuit_monaco"
            "baku" -> "circuit_baku"
            "spa" -> "circuit_spa"
            "zandvoort" -> "circuit_zandvoort"
            "monza" -> "circuit_monza"
            "marina_bay" -> "circuit_marina_bay"
            "suzuka" -> "circuit_suzuka"
            "americas" -> "circuit_americas"
            "vegas" -> "circuit_vegas"
            "jeddah" -> "circuit_jeddah"
            "hungaroring" -> "circuit_hungaroring"
            "villeneuve" -> "circuit_villeneuve"
            "silverstone" -> "circuit_silverstone"
            "red_bull_ring" -> "circuit_red_bull_ring"
            "paul_ricard" -> "circuit_paul_ricard"
            "interlagos" -> "circuit_interlagos"
            "yas_marina" -> "circuit_yas_marina"
            "miami" -> "circuit_miami"
            "losail" -> "circuit_losail"
            "shanghai" -> "circuit_shanghai"
            "imola" -> "circuit_imola"
            "portimao" -> "circuit_portimao"
            "istanbul" -> "circuit_istanbul"
            "nurburgring" -> "circuit_nurburgring"
            "hockenheim" -> "circuit_hockenheim"
            "sochi" -> "circuit_sochi"
            "kyalami" -> "circuit_kyalami"
            "bahrain_outer" -> "circuit_bahrain_outer"
            "sepang" -> "circuit_sepang"
            "hanoi" -> "circuit_hanoi"
            "buddh" -> "circuit_buddh"
            "valencia" -> "circuit_valencia"
            "yeongam" -> "circuit_yeongam"
            "fuji" -> "circuit_fuji"
            "adelaide" -> "circuit_adelaide"
            "mugello" -> "circuit_mugello"
            else -> null
        }

        if (base == null) {
            return CircuitDescription(
                main = context.getString(R.string.circuit_no_description),
                characteristics = "-",
                challenges = "-",
                trivia = "-",
                drs = "-"
            )
        }

        return CircuitDescription(
            main = getStringByName(context, "${base}_main", R.string.circuit_no_description),
            characteristics = getStringByName(context, "${base}_characteristics"),
            challenges = getStringByName(context, "${base}_challenges"),
            trivia = getStringByName(context, "${base}_trivia"),
            drs = getStringByName(context, "${base}_drs")
        )
    }

    private fun getStringByName(context: Context, name: String, fallbackRes: Int? = null): String {
        val resId = context.resources.getIdentifier(name, "string", context.packageName)
        if (resId != 0) {
            return context.getString(resId)
        }
        return fallbackRes?.let { context.getString(it) } ?: "-"
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
