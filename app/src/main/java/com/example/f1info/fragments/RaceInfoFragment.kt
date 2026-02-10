package com.example.f1info.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.f1info.R
import com.example.f1info.api.JolpicaApiService
import com.example.f1info.api.JolpicaClient
import com.example.f1info.api.OpenF1ApiService
import com.example.f1info.api.OpenF1Client
import com.example.f1info.api.OpenF1Repository
import com.example.f1info.databinding.FragmentRaceInfoBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.Locale

class RaceInfoFragment : Fragment() {
    private var _binding: FragmentRaceInfoBinding? = null
    private lateinit var openF1Api: OpenF1ApiService
    private lateinit var jolpicaApi: JolpicaApiService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRaceInfoBinding.inflate(inflater, container, false)
        return _binding?.root ?: throw IllegalStateException("Binding is null in onCreateView")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openF1Api = OpenF1Client.getInstance().create(OpenF1ApiService::class.java)
        jolpicaApi = JolpicaClient.getInstance().create(JolpicaApiService::class.java)
        _binding?.swipeRefresh?.setOnRefreshListener {
            loadCurrentRaceInfo()
        }
        loadCurrentRaceInfo()
    }

    private fun loadCurrentRaceInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                _binding?.progressBar?.visibility = View.VISIBLE
                val season = getSelectedSeason()
                if (season >= 2023) {
                    loadOpenF1RaceInfo(season)
                } else {
                    loadErgastRaceInfo(season)
                }
            } catch (e: Exception) {
                if (e.message != "Job was cancelled") {
                    context?.let {
                        Toast.makeText(
                            it,
                            getString(R.string.error_prefix, e.message ?: ""),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } finally {
                _binding?.progressBar?.visibility = View.GONE
                _binding?.swipeRefresh?.isRefreshing = false
            }
        }
    }

    private fun getSelectedSeason(): Int {
        val prefs = requireContext().getSharedPreferences("F1_PREFS", Context.MODE_PRIVATE)
        val selected = prefs.getString("selected_season", "2026") ?: "2026"
        return selected.toIntOrNull() ?: 2026
    }

    private suspend fun loadOpenF1RaceInfo(season: Int) {
        val session = OpenF1Repository.getLatestRaceSession(openF1Api, season)
        if (session == null) {
            showNoData()
            return
        }

        val sessionKey = session.session_key
        val drivers = openF1Api.getDrivers(sessionKey)
        val results = openF1Api.getSessionResult(sessionKey)

        val driversByNumber = drivers.associateBy { it.driver_number }
        val topThreeText = if (results.isNotEmpty()) {
            buildString {
                append(getString(R.string.top_three_header))
                append("\n")
                results.sortedBy { it.position }.take(3).forEach { result ->
                    val driver = driversByNumber[result.driver_number]
                    val name = driver?.full_name
                        ?: listOfNotNull(driver?.first_name, driver?.last_name)
                            .joinToString(" ")
                            .ifBlank { getString(R.string.label_driver) }
                    val team = driver?.team_name ?: getString(R.string.label_unknown)
                    append("${result.position}. $name ($team)\n")
                }
            }
        } else {
            getString(R.string.results_not_available)
        }

        val displayDate = formatOpenF1Date(session.date_start)
        val sessionName = session.meeting_name ?: session.session_name
        val trackName = session.circuit_short_name ?: session.location ?: session.country_name ?: ""

        updateUI(sessionName, trackName, displayDate, topThreeText)
    }

    private suspend fun loadErgastRaceInfo(season: Int) {
        val response = jolpicaApi.getLastRaceResults(season.toString())
        val race = response.MRData.RaceTable.Races.firstOrNull()
        if (race == null) {
            showNoData()
            return
        }

        val topThreeText = if (race.Results.isNotEmpty()) {
            buildString {
                append(getString(R.string.top_three_header))
                append("\n")
                race.Results.sortedBy { it.position.toIntOrNull() ?: 0 }.take(3).forEach { result ->
                    val name = "${result.Driver.givenName} ${result.Driver.familyName}"
                    val team = result.Constructor.name
                    append("${result.position}. $name ($team)\n")
                }
            }
        } else {
            getString(R.string.results_not_available)
        }

        val displayDate = formatErgastDate(race.date, race.time)
        updateUI(race.raceName, race.Circuit.circuitName, displayDate, topThreeText)
    }

    private fun updateUI(sessionName: String, trackName: String, dateText: String, topThreeText: String) {
        _binding?.apply {
            tvNoData.visibility = View.GONE
            cardRaceInfo.visibility = View.VISIBLE
            tvSessionName.text = sessionName
            tvTrackName.text = trackName
            tvDate.text = dateText
            tvTopThree.text = topThreeText
        }
    }

    private fun showNoData() {
        _binding?.tvNoData?.visibility = View.VISIBLE
        _binding?.cardRaceInfo?.visibility = View.GONE
    }

    private fun formatOpenF1Date(dateStart: String): String {
        return try {
            val dateTime = OffsetDateTime.parse(dateStart).atZoneSameInstant(ZoneId.systemDefault())
            val formatter =
                java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", Locale.getDefault())
            dateTime.format(formatter)
        } catch (_: Exception) {
            dateStart
        }
    }

    private fun formatErgastDate(date: String, time: String?): String {
        return try {
            val full = if (!time.isNullOrBlank()) "$date $time" else date
            val input = if (!time.isNullOrBlank()) "yyyy-MM-dd HH:mm:ss'Z'" else "yyyy-MM-dd"
            val sdf = SimpleDateFormat(input, Locale.getDefault())
            val output = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            val parsed = sdf.parse(full)
            if (parsed != null) output.format(parsed) else date
        } catch (_: Exception) {
            date
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
