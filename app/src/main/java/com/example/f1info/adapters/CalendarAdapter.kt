package com.example.f1info.adapters
import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.f1info.CircuitDetailsActivity
import com.example.f1info.R
import com.example.f1info.models.Race
import java.text.SimpleDateFormat
import java.util.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CalendarAdapter(private val races: List<Race>) : RecyclerView.Adapter<CalendarAdapter.RaceViewHolder>() {

    class RaceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val raceName: TextView = view.findViewById(R.id.tvRaceName)
        val country: TextView = view.findViewById(R.id.tvCountry)
        val date: TextView = view.findViewById(R.id.tvDate)
    }

    private fun getFlagEmoji(country: String): String {
        return when (country.lowercase()) {
            "italy" -> "🇮🇹" // 🇮🇹
            "uk" -> "🇬🇧" // 🇬🇧
            "monaco" -> "🇲🇨" // 🇲🇨
            "belgium" -> "🇧🇪" // 🇧🇪
            "france" -> "🇫🇷" // 🇫🇷
            "germany" -> "🇩🇪" // 🇩🇪
            "netherlands" -> "🇳🇱" // 🇳🇱
            "spain" -> "🇪🇸" // 🇪🇸
            "usa" -> "🇺🇸" // 🇺🇸
            "mexico" -> "🇲🇽" // 🇲🇽
            "brazil" -> "🇧🇷" // 🇧🇷
            "japan" -> "🇯🇵" // 🇯🇵
            "australia" -> "🇦🇺" // 🇦🇺
            "canada" -> "🇨🇦" // 🇨🇦
            "china" -> "🇨🇳" // 🇨🇳
            "austria" -> "🇦🇹" // 🇦🇹
            "azerbaijan" -> "🇺🇿" // 🇺🇿
            "qatar" -> "🇶🇦" // 🇶🇦
            "saudi arabia" -> "🇸🇦" // 🇸🇦
            "singapore" -> "🇸🇬" // 🇸🇬
            "south africa" -> "🇿🇦" // 🇿🇦
            "argentina" -> "🇦🇷" // 🇦🇷
            "turkey" -> "🇹🇷" // 🇹🇷
            "portugal" -> "🇵🇹" // 🇵🇹
            "south korea" -> "🇰🇷" // 🇰🇷
            "uae" -> "🇦🇪" // 🇦🇪
            "hungary" -> "🇭🇷" // 🇭🇷
            "bahrain" -> "🇧🇭" // 🇧🇭
            else -> ""
        }
    }



    private fun getCircuitDescription(circuitId: String): String {
        return when (circuitId.lowercase()) {
            "albert_park" -> "Melbourne to tor uliczny z szybkimi sekcjami i technicznymi zakrętami. Znany z malowniczej lokalizacji wokół jeziora Albert Park."
            "bahrain" -> "Bahrain International Circuit to pierwszy tor na Bliskim Wschodzie w F1. Znany z wyścigów nocnych i wymagających sekcji s-z."
            "catalunya" -> "Circuit de Barcelona-Catalunya to tor testowy zespołów F1. Łączy szybkie zakręty z technicznymi sekcjami."
            "hungaroring" -> "Hungaroring w Budapeszcie jest ciasny, techniczny i często porównywany do toru kartingowego. Nazywany 'Monakiem bez murków'."
            "monte carlo" -> "Monaco to najwolniejszy, ale najbardziej prestiżowy tor w sezonie. Wyścig uliczny wśród luksusu, gdzie błędy są bezwzględnie karane."
            "baku" -> "Baku City Circuit to najszybszy tor uliczny w kalendarzu. Mieszanka wąskich średniowiecznych uliczek i długiej prostej."
            "villeneuve" -> "Circuit Gilles Villeneuve w Montrealu to tor wyspowy znany z 'Ściany Mistrzów' i dynamicznych walk na prostej startowej."
            "silverstone" -> "Silverstone to miejsce narodzin Formuły 1. Klasyczny brytyjski tor z szybkimi zakrętami jak Maggots-Becketts-Chapel."
            "red_bull_ring" -> "Red Bull Ring w Austrii to krótki, stromy tor z malowniczym położeniem w górach. Znany z trzech długich prostych."
            "paul_ricard" -> "Circuit Paul Ricard to francuski tor znany z charakterystycznych niebiesko-czerwonych stref wyjazdowych i dużej płytowości."
            "spa" -> "Spa-Francorchamps to kultowy tor w Belgii z zakrętem Eau Rouge. Uwielbiany przez kierowców za szybkie, płynne sekcje."
            "zandvoort" -> "Circuit Zandvoort to holenderski tor nad morzem. Znany z bankedowanych zakrętów i piaszczystego otoczenia."
            "monza" -> "Monza to jeden z najstarszych i najszybszych torów w kalendarzu F1. Znany jako Świątynia Prędkości z długimi prostymi i Parabolica."
            "marina_bay" -> "Marina Bay Street Circuit to nocny wyścig uliczny w Singapurze. Jeden z najbardziej wymagających fizycznie torów w kalendarzu."
            "suzuka" -> "Suzuka to japoński tor w kształcie ósemki. Kultowe zakręty jak 130R i Sekcja Esses testują umiejętności kierowców."
            "americas" -> "Circuit of the Americas w Austin to nowoczesny tor inspirowany najlepszymi sekcjami z całego świata, w tym słynnym zakrętem Eau Rouge."
            "rodriguez" -> "Autódromo Hermanos Rodríguez w Meksyku to wysoko położony tor z charakterystycznym zakrętem na stadionie Foro Sol."
            "interlagos" -> "Interlagos w São Paulo to stromy, wymagający tor. Znany z dynamicznej pogody i emocjonujących finiszów sezonu."
            "yas_marina" -> "Tor Yas Marina w Abu Zabi to nowoczesny obiekt znany z wieczornych wyścigów, luksusowej otoczki i hotelu nad torem."
            "jeddah" -> "Jeddah Corniche Circuit to najszybszy tor uliczny F1 z wieloma szybkimi zakrętami. Znany z niebezpiecznego układu i wysokich prędkości."
            "miami" -> "Miami International Autodrome to nowy tor uliczny wokół Hard Rock Stadium. Łączy szybkie sekcje z technicznymi zakrętami."
            "losail" -> "Losail International Circuit w Katarze to tor motocyklowy przystosowany dla F1. Znany z szybkiego, płynnego układu."
            "vegas" -> "Las Vegas Strip Circuit to nocny wyścig uliczny w sercu miasta. Charakterystyczne dla niego są długie proste i ciasne zakręty."
            "shanghai" -> "Shanghai International Circuit to tor o kształcie znaku '上' (Shang). Znany z długiej prostej i wymagającego zakrętu 1-2-3."
            "imola" -> "Imola to klasyczny włoski tor o bogatej historii. Wymagający układ z sekcjami jak Acque Minerali i Rivazza."
            "portimao" -> "Autódromo Internacional do Algarve to stromy, falisty tor w Portugalii. Znany z dynamicznego profilu i śmiałych zakrętów."
            "istanbul" -> "Istanbul Park to turecki tor znany z zakrętu 8 (Turn 8) - jednej z najtrudniejszych sekcji w kalendarzu F1."
            "nurburgring" -> "Nürburgring to legendarny niemiecki tor. Choć F1 używa krótkiego układu, miejsce to ma bogatą historię wyścigową."
            "hockenheim" -> "Hockenheimring to kolejny klasyczny niemiecki tor. Znany z długich prostych przez las i charakterystycznego stadium."
            "sochi" -> "Sochi Autodrom to tor olimpijski z mieszanką średnio-szybkich zakrętów. Znany z długiej prostej wzdłuż nadmorskiego bulwaru."
            "kyalami" -> "Kyalami to południowoafrykański tor o bogatej historii. Charakteryzuje się szybkimi zakrętami i stromym profilem."
            else -> "Brak opisu dla tego toru."
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
                putExtra("description", getCircuitDescription(race.circuitId))
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
    } catch (e: Exception) {
        dateString // fallback
    }
}
