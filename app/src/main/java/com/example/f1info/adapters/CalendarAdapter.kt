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
import com.example.f1info.models.CircuitDescription
import com.example.f1info.models.Race
import java.text.SimpleDateFormat
import java.util.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Suppress("CAST_NEVER_SUCCEEDS")
class CalendarAdapter(private val races: List<Race>) : RecyclerView.Adapter<CalendarAdapter.RaceViewHolder>() {

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
            "azerbaijan" -> "🇺🇿"
            "qatar" -> "🇶🇦"
            "saudi arabia" -> "🇸🇦"
            "singapore" -> "🇸🇬"
            "south africa" -> "🇿🇦"
            "argentina" -> "🇦🇷"
            "turkey" -> "🇹🇷"
            "portugal" -> "🇵🇹"
            "south korea" -> "🇰🇷"
            "uae" -> "🇦🇪"
            "hungary" -> "🇭🇷"
            "bahrain" -> "🇧🇭"
            else -> ""
        }
    }

    private fun getCircuitDescription(circuitId: String): CircuitDescription {
        return when (circuitId.lowercase()) {
            "albert_park" -> CircuitDescription(
                main = "Melbourne Grand Prix Circuit (Albert Park) to tymczasowy tor uliczny o długości 5.303 km z 14 zakrętami.",
                charakterystyka = "Mieszanka średnio-szybkich sekcji z technicznymi zakrętami. Po modyfikacjach w 2022 stał się szybszy i bardziej płynny.",
                wyzwania = "Wąskie strefy wyjazdowe, śliska nawierzchnia na początku weekendu. Zakręty 11-12 to kluczowy sekundowy duet.",
                ciekawostka = "Jeden z nielicznych torów gdzie widzowie mogą spacerować po większości trasy poza sezonem.",
                drs = "Dwie strefy - na prostej start/meta i między zakrętami 2-3."
            )
            "bahrain" -> CircuitDescription(
                main = "Bahrain International Circuit (Sakhir) to 5.412 km pustynny tor z 15 zakrętami.",
                charakterystyka = "Wymagające hamowania, ścieralne opony, nocne wyścigi od 2014 roku.",
                wyzwania = "Piasek na torze, duże zużycie hamulców, trudna trakcja w zakrętach 9-10.",
                ciekawostka = "Pierwszy tor F1 na Bliskim Wschodzie (od 2004). 'Zewnętrzny' wariant używany w 2020 to najkrótszy tor w historii F1.",
                drs = "Dwie strefy - prosta start/meta i między zakrętami 3-4."
            )
            "catalunya" -> CircuitDescription(
                main = "Circuit de Barcelona-Catalunya to 4.675 km tor testowy z 16 zakrętami.",
                charakterystyka = "Idealny do testów - mieszanka wszystkich typów zakrętów. Szybka sekcja 3-9 i techniczna końcówka.",
                wyzwania = "Zmienne wiatry, trudna trakcja w zakręcie 10, przegrzewanie opon w długim zakręcie 3.",
                ciekawostka = "Ostatni zakręt był wielokrotnie modyfikowany - w 2021 usunięto chicane, wracając do klasycznego profilu.",
                drs = "Dwie strefy - prosta start/meta i przed zakrętem 1."
            )
            "monaco" -> CircuitDescription(
                main = "Circuit de Monaco to 3.337 km najwolniejszy tor uliczny z 19 zakrętami.",
                charakterystyka = "Ciasny, wąski (najwęższy punkt: 8m), zerowy margines błędu. Tunel to jedyne miejsce pełnej prędkości.",
                wyzwania = "Fizycznie najtrudniejszy wyścig - kierowcy wykonują ponad 40 ruchów kierownicą na okrążenie.",
                ciekawostka = "Tylko 3 kierowców w historii kwalifikowało się bez dotykania barier (Senna, Schumacher, Leclerc).",
                drs = "Tylko jedna strefa - tunel do Nouvelle Chicane."
            )
            "baku" -> CircuitDescription(
                main = "Baku City Circuit to 6.003 km najszybszy tor uliczny z 20 zakrętami.",
                charakterystyka = "Mieszanka wąskich średniowiecznych uliczek (szer. 7.6m) i 2.2 km prostej. Zakręt 8 to najostrzejszy zakręt w F1.",
                wyzwania = "Nagłe podmuchy wiatru między budynkami, trudna ocena odległości w sekcji zamkowej.",
                ciekawostka = "Prosta start/meta jest dłuższa niż cały tor w Monaco. W 2017 Vettel celowo uderzył w Hamiltona właśnie tutaj.",
                drs = "Dwie strefy - główna prosta i przed zakrętem 3."
            )
            "spa" -> CircuitDescription(
                main = "Circuit de Spa-Francorchamps to 7.004 km najdłuższy tor F1 z 19 zakrętami.",
                charakterystyka = "Eau Rouge-Raidillon to najbardziej ikoniczna sekcja w F1. Płynne, szybkie sekcje przez ardeńskie lasy.",
                wyzwania = "Mikroklimat - często deszcz pada tylko na połowie toru. Kemmel Straight to raj dla slipstreamingu.",
                ciekawostka = "Pełna wersja toru z lat 20. miała 14 km. W 2022 dodano nowe strefy wyjazdowe po wypadku Huberta.",
                drs = "Dwie strefy - prosta Kemmel i główna prosta."
            )
            "zandvoort" -> CircuitDescription(
                main = "Circuit Zandvoort to 4.259 km nadmorski tor z 14 zakrętami.",
                charakterystyka = "Bankowane zakręty (nawet do 19°), piaszczyste otoczenie, wiatry znad morza.",
                wyzwania = "Utrzymanie prędkości w bankowanym ostatnim zakręcie przed startem następnego okrążenia.",
                ciekawostka = "Tarzan (zakręt 1) ma 18-metrowy promień - najmniejszy w kalendarzu F1. Nazwany od protestującego rolnika.",
                drs = "Tylko jedna strefa - główna prosta."
            )
            "monza" -> CircuitDescription(
                main = "Autodromo Nazionale Monza to 5.793 km 'Świątynia Prędkości' z 11 zakrętami.",
                charakterystyka = "Bolidy pokonują 80% okrążenia na pełnym gazie. Parabolica to ostatni wielki szybki zakręt w F1.",
                wyzwania = "Hamowanie z 350+ km/h do 70 km/h w pierwszej szykanie, walka o trakcję przy wyjściu z zakrętów.",
                ciekawostka = "W 1971 Peter Gethin wygrał z przewagą 0.01s - najciaśniejszy finisz w historii.",
                drs = "Dwie strefy - główna prosta i przed zakrętem 1."
            )
            "marina_bay" -> CircuitDescription(
                main = "Marina Bay Street Circuit to 5.063 km nocny tor uliczny z 23 zakrętami.",
                charakterystyka = "Nocna sauna - 90% pełnego gazu, wilgotność przekraczająca 80%. Sekcja pod mostem to akustyczna maszynka.",
                wyzwania = "Fizyczna wytrzymałość (kierowcy tracą do 3kg wagi), śliskie połączenia między płytami drogowymi.",
                ciekawostka = "Oświetlenie zużywa moc 3,000,000W - wystarczającą dla 1,200 mieszkań.",
                drs = "Dwie strefy - główna prosta i między zakrętami 5-7."
            )
            "suzuka" -> CircuitDescription(
                main = "Suzuka International Racing Course to 5.807 km tor w kształcie ósemki z 18 zakrętami.",
                charakterystyka = "Jedyna prawdziwa ósemka w kalendarzu. Sekcja Esses to 'zegarowy test' dla zawieszenia.",
                wyzwania = "Sekwencje zakrętów gdzie błąd w pierwszym psuje całą sekwencję. Degner to zakręt który 'oszukuje' kierowców.",
                ciekawostka = "W 1989 i 1990 decydował o tytule w słynnych kolizjach Prost-Senna.",
                drs = "Dwie strefy - główna prosta i przed zakrętem 1."
            )
            "americas" -> CircuitDescription(
                main = "Circuit of The Americas to 5.513 km nowoczesny tor z 20 zakrętami.",
                charakterystyka = "Wzniesienia do 40m, sekcja 1 to hołd dla Silverstone, Hockenheim i Suzuka.",
                wyzwania = "Zmienna przyczepność między sektorami, 'ślepe' apex-y w sekcji 12-15.",
                ciekawostka = "Zaprojektowany by promować wyprzedzanie - w 2012 miał rekordowe 20 miejsc do overtake'u.",
                drs = "Dwie strefy - główna prosta i przed zakrętem 12."
            )
            "vegas" -> CircuitDescription(
                main = "Las Vegas Strip Circuit to 6.201 km nocny tor uliczny z 17 zakrętami.",
                charakterystyka = "Nocna jazda wśród neonów - długie proste przerywane ciasnymi zakrętami.",
                wyzwania = "Zimne opony (nocne temperatury), śliskie pasy startowe na skrzyżowaniach.",
                ciekawostka = "W 2023 manhole cover wyrwał się podczas treningu, niszcząc bolidy Sainza i Ocona.",
                drs = "Trzy strefy - najwięcej dla toru ulicznego."
            )
            "jeddah" -> CircuitDescription(
                main = "Jeddah Corniche Circuit to 6.174 km najszybszy tor uliczny z 27 zakrętami.",
                charakterystyka = "Średnia prędkość >250 km/h. 27 zakrętów zaprojektowanych by symulować płynność torów stałych.",
                wyzwania = "'Ślepe' zakręty przy ścianach, wysoka energia zderzeń (np. wypadek Schumachera w 2022).",
                ciekawostka = "Budowany w 8 miesięcy - rekord F1. Zakręt 22 nazwany 'The Lance Stroll'.",
                drs = "Dwie strefy - główna prosta i przed zakrętem 27."
            )
            "hungaroring" -> CircuitDescription(
                main = "Hungaroring to 4.381 km techniczny tor z 14 zakrętami.",
                charakterystyka = "Nazywany 'Monakiem bez murków' - ciasny, kręty, z ograniczonymi możliwościami wyprzedzania.",
                wyzwania = "Wysokie temperatury, kurz na torze, trudna trakcja w zakręcie 4.",
                ciekawostka = "Miejsce pierwszego zwycięstwa Fernando Alonso w 2003 roku (wówczas najmłodszy zwycięzca w historii).",
                drs = "Dwie strefy - główna prosta i przed zakrętem 1."
            )
            "villeneuve" -> CircuitDescription(
                main = "Circuit Gilles Villeneuve to 4.361 km tor wyspowy z 14 zakrętami.",
                charakterystyka = "Znany z 'Ściany Mistrzów' i dynamicznych walk na prostej startowej.",
                wyzwania = "Mocne hamowania i dobre zarządzanie hamulcami, zmienna pogoda nad jeziorem.",
                ciekawostka = "W 2011 miał miejsce najdłuższy wyścig w historii F1 (4h04:39 z powodu opóźnień przez deszcz).",
                drs = "Dwie strefy - główna prosta i przed ostatnim zakrętem."
            )
            "silverstone" -> CircuitDescription(
                main = "Silverstone Circuit to 5.891 km historyczny tor z 18 zakrętami.",
                charakterystyka = "Miejsce pierwszego wyścigu F1 w 1950. Sekcja Maggots-Becketts-Chapel to test precyzji.",
                wyzwania = "Nieprzewidywalna brytyjska pogoda, wysokie prędkości w zakrętach.",
                ciekawostka = "W 2020 odbył się tu wyścig z największą liczbą oponień w historii (5 w jednym wyścigu).",
                drs = "Dwie strefy - główna prosta i przed zakrętem 6."
            )
            "red_bull_ring" -> CircuitDescription(
                main = "Red Bull Ring to 4.318 km krótki, stromy tor z 10 zakrętami.",
                charakterystyka = "Położony w górach, z trzema długimi prostymi i szybkimi zakrętami.",
                wyzwania = "Różnice wysokości, trudna aerodynamika z powodu wysokiego położenia.",
                ciekawostka = "W 2020 odbyły się tu dwa wyścigi w kolejnych tygodniach z powodu pandemii.",
                drs = "Dwie strefy - główna prosta i przed zakrętem 1."
            )
            "paul_ricard" -> CircuitDescription(
                main = "Circuit Paul Ricard to 5.842 km tor z 15 zakrętami.",
                charakterystyka = "Znany z charakterystycznych niebiesko-czerwonych stref wyjazdowych i dużej płytowości.",
                wyzwania = "Monotonny układ toru, trudna ocena odległości z powodu podobnych zakrętów.",
                ciekawostka = "Posiada 167 możliwych konfiguracji toru dzięki modułowemu układowi.",
                drs = "Dwie strefy - główna prosta i przed zakrętem 8."
            )
            "interlagos" -> CircuitDescription(
                main = "Autódromo José Carlos Pace (Interlagos) to 4.309 km wymagający tor z 15 zakrętami.",
                charakterystyka = "Stromy, z dużymi różnicami wysokości. Jazda przeciwnie do ruchu wskazówek zegara.",
                wyzwania = "Zmienna pogoda, fizycznie wymagający ze względu na różnice wysokości.",
                ciekawostka = "Miejsce dramatycznego zdobycia tytułu przez Hamiltona w 2008 roku w ostatnim zakręcie.",
                drs = "Dwie strefy - główna prosta i przed zakrętem 1."
            )
            "yas_marina" -> CircuitDescription(
                main = "Yas Marina Circuit to 5.281 km nocny tor z 16 zakrętami.",
                charakterystyka = "Nowoczesny obiekt z wieczornymi wyścigami i hotelem nad torem.",
                wyzwania = "Sekcja pod hotelem Yas z ostrymi zakrętami, tunel wyjazdowy z pit-lane.",
                ciekawostka = "W 2021 był świadkiem kontrowersyjnego zakończenia sezonu i zdobycia tytułu przez Verstappena.",
                drs = "Dwie strefy - główna prosta i przed zakrętem 7."
            )
            "miami" -> CircuitDescription(
                main = "Miami International Autodrome to 5.412 km tor uliczny z 19 zakrętami.",
                charakterystyka = "Nowy tor wokół Hard Rock Stadium, łączący szybkie sekcje z technicznymi zakrętami.",
                wyzwania = "Śliskie pobocza, sztuczna marina dodająca optycznych złudzeń.",
                ciekawostka = "W 2022 miał miejsce pierwszy wyścig, który wygrał Max Verstappen.",
                drs = "Dwie strefy - główna prosta i przed zakrętem 11."
            )
            "losail" -> CircuitDescription(
                main = "Losail International Circuit to 5.380 km tor z 16 zakrętami.",
                charakterystyka = "Tor motocyklowy przystosowany dla F1, z szybkim, płynnym układem.",
                wyzwania = "Nocne oświetlenie, szeroki tor sprzyjający dynamicznej jeździe.",
                ciekawostka = "W 2021 odbył się tu pierwszy wyścig F1 w Katarze.",
                drs = "Dwie strefy - główna prosta i przed zakrętem 1."
            )
            "shanghai" -> CircuitDescription(
                main = "Shanghai International Circuit to 5.451 km tor w kształcie chińskiego znaku '上' z 16 zakrętami.",
                charakterystyka = "Znany z długiej prostej i wymagającego zakrętu 1-2-3.",
                wyzwania = "Techniczne sekcje i zmienne warunki pogodowe.",
                ciekawostka = "Zaprojektowany by przypominać pierwszy znak nazwy miasta Szanghaj.",
                drs = "Dwie strefy - główna prosta i przed zakrętem 14."
            )
            "imola" -> CircuitDescription(
                main = "Autodromo Enzo e Dino Ferrari (Imola) to 4.909 km klasyczny tor z 19 zakrętami.",
                charakterystyka = "Wąski, falisty tor otoczony drzewami, o bogatej historii.",
                wyzwania = "Brak miejsca na błędy, trudne wyprzedzanie.",
                ciekawostka = "W 1994 miejsce śmiertelnych wypadków Senny i Ratzenbergera.",
                drs = "Jedna strefa - główna prosta."
            )
            "portimao" -> CircuitDescription(
                main = "Autódromo Internacional do Algarve to 4.653 km stromy tor z 15 zakrętami.",
                charakterystyka = "Falisty jak rollercoaster - różnice wysokości do 60m.",
                wyzwania = "Niewidoczne apex-y, trudna ocena odległości.",
                ciekawostka = "W 2020 odbył się tu pierwszy wyścig F1 z powodu pandemii.",
                drs = "Dwie strefy - główna prosta i przed zakrętem 1."
            )
            "istanbul" -> CircuitDescription(
                main = "Istanbul Park to 5.338 km tor z 14 zakrętami.",
                charakterystyka = "Znany z zakrętu 8 (Turn 8) - jednej z najtrudniejszych sekcji w F1 z czterema apeksami.",
                wyzwania = "Zmienna przyczepność na 'młodej' nawierzchni.",
                ciekawostka = "W 2020 Hamilton zdobył tu 7. tytuł, wykonując 'legendarny' poślizg.",
                drs = "Dwie strefy - główna prosta i przed zakrętem 12."
            )
            "nurburgring" -> CircuitDescription(
                main = "Nürburgring GP-Strecke to 5.148 km tor z 15 zakrętami.",
                charakterystyka = "Młodszy brat Nordschleife - techniczna sekcja 'arena' kontrastuje z szybkimi zakrętami w lesie.",
                wyzwania = "Mikroklimat zmieniający się co kilka minut.",
                ciekawostka = "Pełna Nordschleife (20.8 km) nazywana 'Zielonym Piekłem'.",
                drs = "Dwie strefy - główna prosta i przed zakrętem 1."
            )
            "hockenheim" -> CircuitDescription(
                main = "Hockenheimring to 4.574 km tor z 17 zakrętami.",
                charakterystyka = "Historycznie - długie proste przez las. Po 2002 - techniczny tor ze 'stadionowym' finałem.",
                wyzwania = "Hamowanie do hairpinu z 330 km/h.",
                ciekawostka = "W 2000 Barrichello wygrał w deszczu startując z 18. pozycji.",
                drs = "Dwie strefy - główna prosta i przed zakrętem 6."
            )
            "sochi" -> CircuitDescription(
                main = "Sochi Autodrom to 5.848 km tor olimpijski z 19 zakrętami.",
                charakterystyka = "Mieszanka średnio-szybkich zakrętów wzdłuż nadmorskiego bulwaru.",
                wyzwania = "Wiele technicznych zakrętów 90-stopniowych.",
                ciekawostka = "Zbudowany wokół obiektów Olimpiady w Soczi 2014.",
                drs = "Dwie strefy - główna prosta i przed zakrętem 2."
            )
            "kyalami" -> CircuitDescription(
                main = "Kyalami Grand Prix Circuit to 4.529 km tor z 9 zakrętami.",
                charakterystyka = "Położony 1500m n.p.m., szybkie zakręty, stromy profil.",
                wyzwania = "Rzadkie powietrze wpływa na moc silnika i chłodzenie.",
                ciekawostka = "W latach 70. miał niebezpieczną konfigurację z zakrętem Barbeque.",
                drs = "Dwie strefy - główna prosta i przed zakrętem 1."
            )
            "bahrain_outer" -> CircuitDescription(
                main = "Bahrain Outer Track to 3.543 km krótki wariant toru w Bahrajnie.",
                charakterystyka = "Używany jednorazowo w 2020 roku, nazywany 'prawie owalem'.",
                wyzwania = "Bardzo szybkie okrążenia (mniej niż minuta), intensywna walka koło koło.",
                ciekawostka = "Najkrótszy tor w historii F1, stworzony specjalnie na potrzeby pandemicznego sezonu.",
                drs = "Jedna strefa - główna prosta."
            )
            "sepang" -> CircuitDescription(
                main = "Sepang International Circuit to 5.543 km tor z 15 zakrętami.",
                charakterystyka = "Pierwszy 'Tilke-drom' - szerokie zakręty, podwójna prosta.",
                wyzwania = "Ekstremalna wilgotność, nagłe burze monsunowe.",
                ciekawostka = "W 2001 tuż przed startem deszcz był tak silny, że kierowcy czekali 50 minut.",
                drs = "Dwie strefy - obie na podwójnej prostej start/meta."
            )
            "hanoi" -> CircuitDescription(
                main = "Hanoi Street Circuit to 5.607 km planowany tor uliczny z 23 zakrętami.",
                charakterystyka = "Miał łączyć długie proste z technicznymi sekcjami inspirowanymi Suzuka i Monaco.",
                wyzwania = "Wysoka wilgotność i temperatury typowe dla Wietnamu.",
                ciekawostka = "Nigdy nie gościł F1 z powodu pandemii, mimo pełnego przygotowania toru.",
                drs = "Planowane trzy strefy DRS."
            )
            "buddh" -> CircuitDescription(
                main = "Buddh International Circuit to 5.125 km tor z 16 zakrętami.",
                charakterystyka = "Szeroki układ sprzyjający wyprzedzaniu, z charakterystycznym zakrętem 10-11.",
                wyzwania = "Wysokie temperatury i zapylenie typowe dla regionu Delhi.",
                ciekawostka = "Gościł F1 w latach 2011-2013, potem usunięty z kalendarza z powodów finansowych.",
                drs = "Dwie strefy - główna prosta i przed zakrętem 4."
            )
            "valencia" -> CircuitDescription(
                main = "Valencia Street Circuit to 5.419 km tor uliczny z 25 zakrętami.",
                charakterystyka = "Biegł wokół portu w Walencji, z widokami na Morze Śródziemne.",
                wyzwania = "Wąskie sekcje między barierami, mało miejsca na błędy.",
                ciekawostka = "Miejsce słynnego wyprzedzania Fernando Alonso na 130R w 2012 roku.",
                drs = "Dwie strefy - główna prosta i przed zakrętem 12."
            )
            "yeongam" -> CircuitDescription(
                main = "Korean International Circuit to 5.615 km tor z 18 zakrętami.",
                charakterystyka = "Mieszanka szybkich zakrętów i technicznych sekcji, z pierwszym sektorem przypominającym tor stały.",
                wyzwania = "Niska frekwencja i problemy organizacyjne.",
                ciekawostka = "Gościł F1 tylko w latach 2010-2013, potem porzucony z powodu braku zainteresowania.",
                drs = "Dwie strefy - główna prosta i przed zakrętem 1."
            )
            "fuji" -> CircuitDescription(
                main = "Fuji Speedway to 4.563 km tor z 16 zakrętami.",
                charakterystyka = "Położony u podnóża góry Fuji, z bardzo długą prostą.",
                wyzwania = "Zmienne warunki pogodowe typowe dla regionu.",
                ciekawostka = "Ostatni raz gościł F1 w 2008 roku w pamiętnym wyścigu w strugach deszczu.",
                drs = "Dwie strefy - główna prosta i przed zakrętem 10."
            )
            "adelaide" -> CircuitDescription(
                main = "Adelaide Street Circuit to 3.780 km tor uliczny z 16 zakrętami.",
                charakterystyka = "Słynny z ciasnych zakrętów i charakteru bardzo wymagającego dla kierowców.",
                wyzwania = "Wąskie uliczki z minimalnym marginesem błędu.",
                ciekawostka = "Miejsce dramatycznych decyzji o tytułach mistrzowskich w latach 1986 i 1994.",
                drs = "Brak stref DRS (tor używany przed erą DRS)."
            )
            "mugello" -> CircuitDescription(
                main = "Mugello Circuit to 5.245 km tor z 15 zakrętami.",
                charakterystyka = "Tor testowy Ferrari, z szybkimi, płynnymi zakrętami i dużymi różnicami wysokości.",
                wyzwania = "Wysokie prędkości w zakrętach, wymagające idealnej linii.",
                ciekawostka = "Gościł F1 jednorazowo w 2020 roku z powodu pandemii, w wyścigu z rekordową liczbą karanych kierowców.",
                drs = "Dwie strefy - główna prosta i przed zakrętem 1."
            )
            else -> CircuitDescription(
                main = "Brak opisu tego toru.",
                charakterystyka = "-",
                wyzwania = "-",
                ciekawostka = "-",
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
                putExtra("description", getCircuitDescription(race.circuitId) as java.io.Serializable)
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
