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
        return when (circuitId.lowercase()) {
            "albert_park" -> CircuitDescription(
                main = "Melbourne Grand Prix Circuit (Albert Park) is a temporary street circuit, 5.303 km with 14 turns.",
                characteristics = "A blend of medium-fast sections and technical corners. After the 2022 changes it became faster and more flowing.",
                challenges = "Narrow runoff areas, slippery surface early in the weekend. Turns 11-12 are a key double corner.",
                trivia = "One of the few circuits where spectators can walk much of the track outside the season.",
                drs = "Two zones - main straight and between Turns 2-3."
            )
            "bahrain" -> CircuitDescription(
                main = "Bahrain International Circuit (Sakhir) is a 5.412 km desert track with 15 turns.",
                characteristics = "Heavy braking, abrasive asphalt, night races since 2014.",
                challenges = "Sand on track, high brake wear, tricky traction in Turns 9-10.",
                trivia = "First F1 track in the Middle East (since 2004). The outer layout used in 2020 was the shortest in F1 history.",
                drs = "Two zones - main straight and between Turns 3-4."
            )
            "catalunya" -> CircuitDescription(
                main = "Circuit de Barcelona-Catalunya is a 4.675 km test track with 16 turns.",
                characteristics = "Great for testing with a mix of all corner types. Fast sector 3-9 and a technical final sector.",
                challenges = "Variable winds, traction out of Turn 10, tire overheating in the long Turn 3.",
                trivia = "The final corner has been modified multiple times; in 2021 the chicane was removed, returning to the classic profile.",
                drs = "Two zones - main straight and before Turn 1."
            )
            "monaco" -> CircuitDescription(
                main = "Circuit de Monaco is a 3.337 km street circuit and the slowest on the calendar, with 19 turns.",
                characteristics = "Tight and narrow (narrowest point: 8m) with zero margin for error. The tunnel is the only flat-out section.",
                challenges = "Physically one of the toughest races - drivers make over 40 steering inputs per lap.",
                trivia = "Only three drivers in history qualified without touching the barriers (Senna, Schumacher, Leclerc).",
                drs = "One zone - tunnel to Nouvelle Chicane."
            )
            "baku" -> CircuitDescription(
                main = "Baku City Circuit is a 6.003 km street circuit with 20 turns and very high speeds.",
                characteristics = "Mix of narrow medieval streets (7.6m wide) and a 2.2 km straight. Turn 8 is the tightest in F1.",
                challenges = "Sudden wind gusts between buildings and hard distance judgment in the castle section.",
                trivia = "The start/finish straight is longer than the entire Monaco circuit. In 2017 Vettel deliberately hit Hamilton here.",
                drs = "Two zones - main straight and before Turn 3."
            )
            "spa" -> CircuitDescription(
                main = "Circuit de Spa-Francorchamps is a 7.004 km track, the longest in F1, with 19 turns.",
                characteristics = "Eau Rouge-Raidillon is the most iconic section in F1. Flowing, fast corners through the Ardennes forest.",
                challenges = "Microclimate - rain often falls on only half the track. The Kemmel Straight is a slipstreaming hotspot.",
                trivia = "The full 1920s layout was 14 km. New runoff areas were added in 2022 after the Hubert crash.",
                drs = "Two zones - Kemmel straight and the main straight."
            )
            "zandvoort" -> CircuitDescription(
                main = "Circuit Zandvoort is a 4.259 km seaside track with 14 turns.",
                characteristics = "Banked corners (up to 19 degrees), sandy surroundings, and sea winds.",
                challenges = "Maintaining speed through the banked final corner onto the straight.",
                trivia = "Tarzan (Turn 1) has an 18-meter radius, the smallest on the F1 calendar, named after a protesting farmer.",
                drs = "One zone - main straight."
            )
            "monza" -> CircuitDescription(
                main = "Autodromo Nazionale Monza is a 5.793 km track, the 'Temple of Speed', with 11 turns.",
                characteristics = "Cars run about 80% of the lap at full throttle. Parabolica is the last big fast corner in F1.",
                challenges = "Braking from 350+ km/h to 70 km/h into the first chicane, traction on corner exits.",
                trivia = "In 1971 Peter Gethin won by 0.01s, the closest finish in F1 history.",
                drs = "Two zones - main straight and before Turn 1."
            )
            "marina_bay" -> CircuitDescription(
                main = "Marina Bay Street Circuit is a 5.063 km night street track with 23 turns.",
                characteristics = "A night-time sauna - 90% full throttle, humidity over 80%. The bridge section is an acoustic highlight.",
                challenges = "Physical endurance (drivers can lose up to 3 kg), slippery joins between road plates.",
                trivia = "Lighting draws 3,000,000W, enough for around 1,200 homes.",
                drs = "Two zones - main straight and between Turns 5-7."
            )
            "suzuka" -> CircuitDescription(
                main = "Suzuka International Racing Course is a 5.807 km figure-eight track with 18 turns.",
                characteristics = "The only true figure-eight in the calendar. The Esses are a precision test for the suspension.",
                challenges = "Corner sequences where a mistake early ruins the entire section. Degner is a corner that tricks drivers.",
                trivia = "The 1989 and 1990 races decided the title with the famous Prost-Senna collisions.",
                drs = "Two zones - main straight and before Turn 1."
            )
            "americas" -> CircuitDescription(
                main = "Circuit of The Americas is a 5.513 km modern track with 20 turns.",
                characteristics = "Elevation changes up to 40m; sector 1 is a tribute to Silverstone, Hockenheim, and Suzuka.",
                challenges = "Changing grip between sectors and blind apexes in the 12-15 section.",
                trivia = "Designed to promote overtaking; the 2012 race had a record 20 passes.",
                drs = "Two zones - main straight and before Turn 12."
            )
            "vegas" -> CircuitDescription(
                main = "Las Vegas Strip Circuit is a 6.201 km night street circuit with 17 turns.",
                characteristics = "Night racing among neon lights with long straights broken by tight corners.",
                challenges = "Cold tires in night temperatures and slippery painted crossings.",
                trivia = "In 2023 a manhole cover came loose in practice, damaging the Sainz and Ocon cars.",
                drs = "Three zones - the most for a street circuit."
            )
            "jeddah" -> CircuitDescription(
                main = "Jeddah Corniche Circuit is a 6.174 km street track with 27 turns and very high average speeds.",
                characteristics = "Average speed above 250 km/h. The 27 turns are designed to feel like a flowing permanent circuit.",
                challenges = "Blind corners next to walls and high-impact crashes (e.g., Schumacher in 2022).",
                trivia = "Built in 8 months, an F1 record. Turn 22 is nicknamed 'The Lance Stroll'.",
                drs = "Two zones - main straight and before Turn 27."
            )
            "hungaroring" -> CircuitDescription(
                main = "Hungaroring is a 4.381 km technical track with 14 turns.",
                characteristics = "Often called 'Monaco without walls' - tight, twisty, and hard to overtake.",
                challenges = "High temperatures, dusty surface, and tricky traction in Turn 4.",
                trivia = "Fernando Alonso took his first win here in 2003 (then the youngest winner).",
                drs = "Two zones - main straight and before Turn 1."
            )
            "villeneuve" -> CircuitDescription(
                main = "Circuit Gilles Villeneuve is a 4.361 km island circuit with 14 turns.",
                characteristics = "Famous for the 'Wall of Champions' and intense battles on the main straight.",
                challenges = "Heavy braking and brake management, with changeable lake-side weather.",
                trivia = "The 2011 race was the longest in F1 history (4h04:39 due to rain delays).",
                drs = "Two zones - main straight and before the last corner."
            )
            "silverstone" -> CircuitDescription(
                main = "Silverstone Circuit is a 5.891 km historic track with 18 turns.",
                characteristics = "Site of the first F1 race in 1950. Maggots-Becketts-Chapel is a precision test.",
                challenges = "Unpredictable British weather and very high-speed corners.",
                trivia = "In 2020 it saw a record number of tire failures (five in one race).",
                drs = "Two zones - main straight and before Turn 6."
            )
            "red_bull_ring" -> CircuitDescription(
                main = "Red Bull Ring is a short, steep 4.318 km track with 10 turns.",
                characteristics = "Mountain setting with three long straights and fast corners.",
                challenges = "Elevation changes and aero efficiency at altitude.",
                trivia = "Two races were held on consecutive weeks in 2020 due to the pandemic.",
                drs = "Two zones - main straight and before Turn 1."
            )
            "paul_ricard" -> CircuitDescription(
                main = "Circuit Paul Ricard is a 5.842 km track with 15 turns.",
                characteristics = "Known for its blue-and-red runoff areas and extensive tarmac.",
                challenges = "A monotonous layout and hard distance judgment because of similar-looking corners.",
                trivia = "It has 167 possible configurations thanks to its modular layout.",
                drs = "Two zones - main straight and before Turn 8."
            )
            "interlagos" -> CircuitDescription(
                main = "Autodromo Jose Carlos Pace (Interlagos) is a 4.309 km track with 15 turns.",
                characteristics = "Steep with big elevation changes, run counter-clockwise.",
                challenges = "Changeable weather and physical demands due to elevation changes.",
                trivia = "Site of Hamilton's dramatic 2008 title win at the final corner.",
                drs = "Two zones - main straight and before Turn 1."
            )
            "yas_marina" -> CircuitDescription(
                main = "Yas Marina Circuit is a 5.281 km night track with 16 turns.",
                characteristics = "A modern venue with twilight races and a hotel over the circuit.",
                challenges = "The Yas Hotel section has tight corners and a pit-lane exit tunnel.",
                trivia = "The 2021 race saw a controversial season finale and Verstappen's title win.",
                drs = "Two zones - main straight and before Turn 7."
            )
            "miami" -> CircuitDescription(
                main = "Miami International Autodrome is a 5.412 km street circuit with 19 turns.",
                characteristics = "A new track around Hard Rock Stadium combining fast sections with technical corners.",
                challenges = "Slippery runoffs and a fake marina that creates optical illusions.",
                trivia = "The first race was held in 2022 and won by Max Verstappen.",
                drs = "Two zones - main straight and before Turn 11."
            )
            "losail" -> CircuitDescription(
                main = "Losail International Circuit is a 5.380 km track with 16 turns.",
                characteristics = "A motorcycle circuit adapted for F1 with a fast, flowing layout.",
                challenges = "Night lighting and a wide track that encourages dynamic racing.",
                trivia = "The first F1 race in Qatar was held here in 2021.",
                drs = "Two zones - main straight and before Turn 1."
            )
            "shanghai" -> CircuitDescription(
                main = "Shanghai International Circuit is a 5.451 km track shaped like the Chinese character 'Shang', with 16 turns.",
                characteristics = "Known for its long straight and the demanding Turns 1-2-3 sequence.",
                challenges = "Technical sections and changeable weather conditions.",
                trivia = "Designed to resemble the first character of the city name Shanghai.",
                drs = "Two zones - main straight and before Turn 14."
            )
            "imola" -> CircuitDescription(
                main = "Autodromo Enzo e Dino Ferrari (Imola) is a 4.909 km classic track with 19 turns.",
                characteristics = "Narrow and undulating, tree-lined, with a rich history.",
                challenges = "Little room for mistakes and difficult overtaking.",
                trivia = "The 1994 weekend saw the fatal accidents of Senna and Ratzenberger.",
                drs = "One zone - main straight."
            )
            "portimao" -> CircuitDescription(
                main = "Autodromo Internacional do Algarve is a 4.653 km rollercoaster-like track with 15 turns.",
                characteristics = "Undulating layout with elevation changes up to 60m.",
                challenges = "Blind apexes and difficult distance judgment.",
                trivia = "Hosted its first F1 race in 2020 due to the pandemic.",
                drs = "Two zones - main straight and before Turn 1."
            )
            "istanbul" -> CircuitDescription(
                main = "Istanbul Park is a 5.338 km track with 14 turns.",
                characteristics = "Known for Turn 8, one of the toughest sections in F1 with four apexes.",
                challenges = "Variable grip on a 'green' surface.",
                trivia = "In 2020 Hamilton sealed his seventh title here with a legendary slide.",
                drs = "Two zones - main straight and before Turn 12."
            )
            "nurburgring" -> CircuitDescription(
                main = "Nurburgring GP-Strecke is a 5.148 km track with 15 turns.",
                characteristics = "The younger sibling of the Nordschleife; the technical stadium section contrasts with fast forest corners.",
                challenges = "A microclimate that changes every few minutes.",
                trivia = "The full Nordschleife (20.8 km) is called the Green Hell.",
                drs = "Two zones - main straight and before Turn 1."
            )
            "hockenheim" -> CircuitDescription(
                main = "Hockenheimring is a 4.574 km track with 17 turns.",
                characteristics = "Historically long forest straights; after 2002 a technical stadium-style finale.",
                challenges = "Braking into the hairpin from 330 km/h.",
                trivia = "In 2000 Barrichello won in the rain from 18th on the grid.",
                drs = "Two zones - main straight and before Turn 6."
            )
            "sochi" -> CircuitDescription(
                main = "Sochi Autodrom is a 5.848 km Olympic park circuit with 19 turns.",
                characteristics = "Mix of medium-speed corners along a seaside boulevard.",
                challenges = "Many technical 90-degree corners.",
                trivia = "Built around the venues of the 2014 Sochi Olympics.",
                drs = "Two zones - main straight and before Turn 2."
            )
            "kyalami" -> CircuitDescription(
                main = "Kyalami Grand Prix Circuit is a 4.529 km track with 9 turns.",
                characteristics = "Located 1,500m above sea level with fast corners and a steep profile.",
                challenges = "Thin air affects engine power and cooling.",
                trivia = "In the 1970s it featured the dangerous Barbeque Bend.",
                drs = "Two zones - main straight and before Turn 1."
            )
            "bahrain_outer" -> CircuitDescription(
                main = "Bahrain Outer Track is a short 3.543 km layout used in 2020.",
                characteristics = "Used once in 2020 and nicknamed 'almost an oval'.",
                challenges = "Very fast laps (under one minute) and intense wheel-to-wheel racing.",
                trivia = "The shortest circuit in F1 history, created for the pandemic season.",
                drs = "One zone - main straight."
            )
            "sepang" -> CircuitDescription(
                main = "Sepang International Circuit is a 5.543 km track with 15 turns.",
                characteristics = "The first Tilke-drome with wide corners and a double straight.",
                challenges = "Extreme humidity and sudden monsoon storms.",
                trivia = "In 2001 the rain before the start was so heavy drivers waited 50 minutes.",
                drs = "Two zones - both on the double start/finish straight."
            )
            "hanoi" -> CircuitDescription(
                main = "Hanoi Street Circuit was a planned 5.607 km street track with 23 turns.",
                characteristics = "Designed to combine long straights with technical sections inspired by Suzuka and Monaco.",
                challenges = "High humidity and temperatures typical of Vietnam.",
                trivia = "Never hosted F1 due to the pandemic despite being fully prepared.",
                drs = "Planned three DRS zones."
            )
            "buddh" -> CircuitDescription(
                main = "Buddh International Circuit is a 5.125 km track with 16 turns.",
                characteristics = "A wide layout that promotes overtaking with the signature Turns 10-11.",
                challenges = "High temperatures and dust typical of the Delhi region.",
                trivia = "Hosted F1 from 2011-2013, then removed from the calendar for financial reasons.",
                drs = "Two zones - main straight and before Turn 4."
            )
            "valencia" -> CircuitDescription(
                main = "Valencia Street Circuit is a 5.419 km street track with 25 turns.",
                characteristics = "Ran around the Valencia harbor with views of the Mediterranean Sea.",
                challenges = "Narrow sections between barriers with little margin for error.",
                trivia = "Site of a famous Fernando Alonso overtake in 2012.",
                drs = "Two zones - main straight and before Turn 12."
            )
            "yeongam" -> CircuitDescription(
                main = "Korean International Circuit is a 5.615 km track with 18 turns.",
                characteristics = "Mix of fast corners and technical sections, with sector 1 resembling a permanent circuit.",
                challenges = "Low attendance and organizational problems.",
                trivia = "Hosted F1 only from 2010-2013, then abandoned due to lack of interest.",
                drs = "Two zones - main straight and before Turn 1."
            )
            "fuji" -> CircuitDescription(
                main = "Fuji Speedway is a 4.563 km track with 16 turns.",
                characteristics = "Located at the base of Mount Fuji with a very long straight.",
                challenges = "Changeable weather conditions typical of the region.",
                trivia = "Last hosted F1 in 2008 in a memorable rain-soaked race.",
                drs = "Two zones - main straight and before Turn 10."
            )
            "adelaide" -> CircuitDescription(
                main = "Adelaide Street Circuit is a 3.780 km street track with 16 turns.",
                characteristics = "Famous for tight corners and a very demanding character for drivers.",
                challenges = "Narrow streets with minimal margin for error.",
                trivia = "Site of dramatic title-deciding races in 1986 and 1994.",
                drs = "No DRS zones (track used before the DRS era)."
            )
            "mugello" -> CircuitDescription(
                main = "Mugello Circuit is a 5.245 km track with 15 turns.",
                characteristics = "Ferrari's test track with fast, flowing corners and large elevation changes.",
                challenges = "High-speed corners that demand a perfect line.",
                trivia = "Hosted F1 once in 2020 due to the pandemic, with a record number of penalties.",
                drs = "Two zones - main straight and before Turn 1."
            )
            else -> CircuitDescription(
                main = context.getString(R.string.circuit_no_description),
                characteristics = "-",
                challenges = "-",
                trivia = "-",
                drs = "-"
            )
        }
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
