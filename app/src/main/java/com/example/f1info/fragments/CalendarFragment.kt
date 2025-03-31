// CalendarFragment.kt
package com.example.f1info.fragments

import android.annotation.SuppressLint
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
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = URL("https://api.jolpi.ca/ergast/f1/2025/races.json").readText()
                val json = JSONObject(response)
                val races = json.getJSONObject("MRData")
                    .getJSONObject("RaceTable")
                    .getJSONArray("Races")

                raceList.clear()

                for (i in 0 until races.length()) {
                    val race = races.getJSONObject(i)
                    val sessionName = race.getString("raceName")
                    val country = race.getJSONObject("Circuit").getJSONObject("Location").getString("country")
                    val date = race.getString("date")
//                    val circuitName = race.getJSONObject("Circuit").getString("circuitName")
                    val circuitId = race.getJSONObject("Circuit").getString("circuitId")

                    raceList.add(Race(sessionName, country, date, circuitId, -1))
                }

                withContext(Dispatchers.Main) {
                    Log.d("CalendarDebug", "Loaded ${raceList.size} races (from Jolpica)")
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e("CalendarDebug", "Error loading Jolpica calendar: ${e.message}", e)
            }
        }
    }
}
