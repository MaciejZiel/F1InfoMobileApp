// CalendarFragment.kt
package com.example.f1info.fragments

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.f1info.R
import com.example.f1info.adapters.CalendarAdapter
import com.example.f1info.models.Race
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.Calendar
import androidx.lifecycle.lifecycleScope
import com.example.f1info.NotificationReceiver
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.jvm.java

class CalendarFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CalendarAdapter
    private val raceList = mutableListOf<Race>()
    private val TEST_MODE = true

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewCalendar)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = CalendarAdapter(raceList)
        recyclerView.adapter = adapter
        fetchCalendarData()
        scheduleSessionNotifications()
        adapter.notifyDataSetChanged()
        return view
    }

    @SuppressLint("NotifyDataSetChanged", "ScheduleExactAlarm", "UseKtx")
    private fun fetchCalendarData() {
        viewLifecycleOwner.lifecycleScope.launch {
            val prefs = requireContext().getSharedPreferences("F1_PREFS", Context.MODE_PRIVATE)
            val selectedSeason = prefs.getString("selected_season", "2025") ?: "2025"

            try {
                val races = withContext(Dispatchers.IO) {
                    val response = URL("https://api.jolpi.ca/ergast/f1/$selectedSeason/races.json").readText()
                    val json = JSONObject(response)
                    json.getJSONObject("MRData")
                        .getJSONObject("RaceTable")
                        .getJSONArray("Races")
                }

                raceList.clear()

                for (i in 0 until races.length()) {
                    val race = races.getJSONObject(i)
                    val sessionName = race.getString("raceName")
                    val country = race.getJSONObject("Circuit").getJSONObject("Location").getString("country")
                    val date = race.getString("date")
                    val circuitId = race.getJSONObject("Circuit").getString("circuitId")

                    raceList.add(Race(sessionName, country, date, circuitId, -1))

                    if (i == 0) {
                        val alarmKey = "alarm_set_for_${sessionName}_${date}"
                        if (!prefs.getBoolean(alarmKey, false)) {
                            try {
                                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                val raceDate = sdf.parse(date)
                                val notifyTime = Calendar.getInstance().apply {
                                    time = raceDate!!
                                    add(Calendar.DATE, -1)
                                    set(Calendar.HOUR_OF_DAY, 9)
                                    set(Calendar.MINUTE, 0)
                                    set(Calendar.SECOND, 0)
                                }

                                val intent = Intent(requireContext(), NotificationReceiver::class.java).apply {
                                    putExtra("race_name", sessionName)
                                }

                                val pendingIntent = PendingIntent.getBroadcast(
                                    requireContext(),
                                    0,
                                    intent,
                                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                                )

                                val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                                alarmManager.setExact(
                                    AlarmManager.RTC_WAKEUP,
                                    notifyTime.timeInMillis,
                                    pendingIntent
                                )

                                prefs.edit().putBoolean(alarmKey, true).apply()
                                Log.d("CalendarDebug", "✅ Powiadomienie ustawione na ${notifyTime.time}")
                            } catch (ex: Exception) {
                                Log.e("CalendarDebug", "Błąd przy ustawianiu alarmu: ${ex.message}", ex)
                            }
                        }
                    }
                }

                adapter.notifyDataSetChanged()
                Log.d("CalendarDebug", "✅ Załadowano ${raceList.size} wyścigów")

            } catch (e: Exception) {
                Log.e("CalendarDebug", "❌ Błąd ładowania kalendarza: ${e.message}", e)
            }
        }
    }





    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleSessionNotifications() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("SessionDebug", "🟢 scheduleSessionNotifications() start")
                val sessionResponse = URL("https://api.openf1.org/v1/sessions").readText()
                Log.d("SessionDebug", "📅 Pobrano sesje: ${sessionResponse.length} znaków")
                val sessions = org.json.JSONArray(sessionResponse)

                val today = if (TEST_MODE) LocalDate.parse("2025-05-18") else LocalDate.now()

                for (i in 0 until sessions.length()) {
                    val session = sessions.getJSONObject(i)
                    val sessionType = session.getString("session_type")
                    val sessionName = session.optString("session_name", "Sesja F1")
                    Log.d("SessionDebug", "⏱ Sesja: $sessionType, start: ${session.optString("session_start", "brak")}")

                    if (!session.has("session_start")) {
                        Log.w("SessionNotify", "⚠️ Pominięto sesję bez session_start (session_key=${session.optString("session_key", "unknown")})")
                        continue
                    }

                    val sessionStartStr = session.getString("session_start")
                    val sessionDate = OffsetDateTime.parse(sessionStartStr).toLocalDate()
                    Log.d("SessionDebug", "🔍 $sessionType - $sessionDate vs today=$today")

                    if (sessionDate == today) {
                        val title = when (sessionType) {
                            "Race" -> "Dziś wyścig!"
                            "Qualifying" -> "Dziś kwalifikacje!"
                            "Sprint" -> "Dziś sprint!"
                            "Sprint Qualifying", "Sprint Shootout" -> "Dziś kwalifikacje do sprintu!"
                            else -> "Dziś sesja F1!"
                        }

                        Log.d("SessionNotify", "🎯 Trafiono sesję na dziś: $sessionType ($sessionName)")

                        val context = requireContext()
                        val notifyTime = Calendar.getInstance().apply {
                            timeInMillis = System.currentTimeMillis() + 10_000
                        }

                        Log.d("SessionNotify", "✅ Ustawiam powiadomienie: $title na ${notifyTime.time}")

                        val intent = Intent(context, NotificationReceiver::class.java).apply {
                            putExtra("race_name", title)
                        }

                        val pendingIntent = PendingIntent.getBroadcast(
                            context,
                            session.getString("session_key").hashCode(),
                            intent,
                            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                        )

                        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                            if (alarmManager.canScheduleExactAlarms()) {
                                alarmManager.setExact(
                                    AlarmManager.RTC_WAKEUP,
                                    notifyTime.timeInMillis,
                                    pendingIntent
                                )
                            }
                        } else {
                            alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                notifyTime.timeInMillis,
                                pendingIntent
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("SessionNotify", "❌ Błąd przy ustawianiu powiadomień: ${e.message}", e)
            }
        }
    }
}