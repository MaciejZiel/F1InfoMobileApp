package com.example.f1info.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.f1info.R
import com.example.f1info.adapters.CalendarAdapter
import com.example.f1info.models.Race
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class CalendarFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CalendarAdapter
    private val raceList = mutableListOf<Race>()

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
        adapter.notifyDataSetChanged()
        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchCalendarData() {
        viewLifecycleOwner.lifecycleScope.launch {
            val prefs = requireContext().getSharedPreferences("F1_PREFS", Context.MODE_PRIVATE)
            val selectedSeason = prefs.getString("selected_season", "2026") ?: "2026"
            val season = selectedSeason.toIntOrNull() ?: 2026

            try {
                suspend fun fetchRaces(targetSeason: Int) = withContext(Dispatchers.IO) {
                    val response =
                        URL("https://api.jolpi.ca/ergast/f1/$targetSeason/races.json").readText()
                    val json = JSONObject(response)
                    json.getJSONObject("MRData")
                        .getJSONObject("RaceTable")
                        .getJSONArray("Races")
                }

                var races = fetchRaces(season)
                var activeSeason = season
                if (races.length() == 0 && season > 1950) {
                    val fallbackSeason = season - 1
                    val fallbackRaces = fetchRaces(fallbackSeason)
                    if (fallbackRaces.length() > 0) {
                        races = fallbackRaces
                        activeSeason = fallbackSeason
                        Toast.makeText(
                            requireContext(),
                            "No data for $season, showing $fallbackSeason",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                raceList.clear()

                for (i in 0 until races.length()) {
                    val race = races.getJSONObject(i)
                    val sessionName = race.getString("raceName")
                    val country = race.getJSONObject("Circuit").getJSONObject("Location").getString("country")
                    val date = race.getString("date")
                    val circuitId = race.getJSONObject("Circuit").getString("circuitId")

                    raceList.add(Race(sessionName, country, date, circuitId, -1))
                }

                adapter.notifyDataSetChanged()
                Log.d("CalendarDebug", "Loaded ${raceList.size} races (season $activeSeason)")

            } catch (e: Exception) {
                Log.e("CalendarDebug", "Calendar load error: ${e.message}", e)
            }
        }
    }
}
