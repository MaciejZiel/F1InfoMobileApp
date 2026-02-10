package com.example.f1info.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.f1info.R
import com.example.f1info.adapters.DriverStandingsAdapter
import com.example.f1info.api.JolpicaApiService
import com.example.f1info.api.JolpicaClient
import com.example.f1info.api.OpenF1ApiService
import com.example.f1info.api.OpenF1Client
import com.example.f1info.api.OpenF1Repository
import com.example.f1info.api.WikipediaImageFetcher
import com.example.f1info.databinding.FragmentDriverStandingsBinding
import com.example.f1info.models.DriverStanding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DriverStandingsFragment : Fragment() {

    private var _binding: FragmentDriverStandingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DriverStandingsAdapter
    private lateinit var openF1Api: OpenF1ApiService
    private lateinit var jolpicaApi: JolpicaApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDriverStandingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openF1Api = OpenF1Client.getInstance().create(OpenF1ApiService::class.java)
        jolpicaApi = JolpicaClient.getInstance().create(JolpicaApiService::class.java)
        setupRecyclerView()

        binding.swipeRefresh.setOnRefreshListener {
            loadDriverStandings(forceRefresh = true)
        }

        loadDriverStandings()
    }

    private fun setupRecyclerView() {
        adapter = DriverStandingsAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        while (binding.recyclerView.itemDecorationCount > 0) {
            binding.recyclerView.removeItemDecorationAt(0)
        }

        val divider = androidx.recyclerview.widget.DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.transparent_divider)!!)
        binding.recyclerView.addItemDecoration(divider)
    }

    private fun loadDriverStandings(forceRefresh: Boolean = false) {
        viewLifecycleOwner.lifecycleScope.launch {
            val bindingRef = _binding ?: return@launch
            try {
                bindingRef.progressBar.visibility = View.VISIBLE

                val gson = Gson()
                val prefs = requireContext().getSharedPreferences("F1_CACHE", Context.MODE_PRIVATE)
                val season = getSelectedSeason()
                val cacheKey = "driver_standings_cache_$season"
                val cachedJson = prefs.getString(cacheKey, null)

                if (!forceRefresh && cachedJson != null) {
                    val type = object : TypeToken<List<DriverStanding>>() {}.type
                    val cachedList: List<DriverStanding> = gson.fromJson(cachedJson, type)
                    adapter.submitList(cachedList)
                    bindingRef.tvNoData.visibility = View.GONE
                }

                val standings = if (season >= 2023) {
                    loadOpenF1DriverStandings(season)
                } else {
                    loadErgastDriverStandings(season, prefs, gson)
                }

                if (standings.isNotEmpty()) {
                    prefs.edit().putString(cacheKey, gson.toJson(standings)).apply()
                    adapter.submitList(standings)
                    bindingRef.tvNoData.visibility = View.GONE
                } else if (cachedJson == null) {
                    bindingRef.tvNoData.visibility = View.VISIBLE
                }

            } catch (e: Exception) {
                Log.e("DriverStandingsFragment", "Blad podczas ladowania danych", e)
                Toast.makeText(requireContext(), "Blad: ${e.message}", Toast.LENGTH_SHORT).show()
                _binding?.tvNoData?.visibility = View.VISIBLE
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

    private suspend fun loadOpenF1DriverStandings(season: Int): List<DriverStanding> {
        val session = OpenF1Repository.getLatestRaceSession(openF1Api, season) ?: return emptyList()
        val sessionKey = session.session_key

        val drivers = openF1Api.getDrivers(sessionKey)
        val standings = openF1Api.getChampionshipDrivers(sessionKey)

        val driversByNumber = drivers.associateBy { it.driver_number }

        return standings
            .mapNotNull { entry ->
                val info = driversByNumber[entry.driver_number] ?: return@mapNotNull null
                val firstName = info.first_name ?: info.full_name?.substringBefore(" ") ?: "Unknown"
                val lastName = info.last_name ?: info.full_name?.substringAfter(" ", "") ?: ""
                DriverStanding(
                    name = firstName,
                    surname = lastName,
                    team = info.team_name ?: "Unknown",
                    position = entry.position_current,
                    points = entry.points_current ?: 0.0,
                    picture_url = info.headshot_url ?: "",
                    driverNumber = info.driver_number
                )
            }
            .sortedBy { it.position }
    }

    private suspend fun loadErgastDriverStandings(
        season: Int,
        prefs: android.content.SharedPreferences,
        gson: Gson
    ): List<DriverStanding> = withContext(Dispatchers.IO) {
        val response = jolpicaApi.getDriverStandings(season.toString())
        val standingsList = response.MRData.StandingsTable.StandingsLists
            .firstOrNull()
            ?.DriverStandings
            ?: emptyList()

        val cacheJson = prefs.getString("driver_image_cache", null)
        val cacheType = object : TypeToken<MutableMap<String, String>>() {}.type
        val imageCache: MutableMap<String, String> =
            if (cacheJson != null) gson.fromJson(cacheJson, cacheType) else mutableMapOf()

        val results = mutableListOf<DriverStanding>()

        for (standing in standingsList) {
            val driver = standing.Driver
            val constructorName = standing.Constructors.firstOrNull()?.name ?: "Unknown"
            val points = standing.points.toDoubleOrNull() ?: 0.0
            val position = standing.position.toIntOrNull() ?: 0

            var imageUrl = imageCache[driver.driverId]
            if (imageUrl.isNullOrBlank() && !driver.url.isNullOrBlank()) {
                val title = WikipediaImageFetcher.extractTitleFromUrl(driver.url)
                if (title != null) {
                    imageUrl = WikipediaImageFetcher.fetchThumbnailUrl(title)
                    if (!imageUrl.isNullOrBlank()) {
                        imageCache[driver.driverId] = imageUrl
                    }
                }
            }

            results.add(
                DriverStanding(
                    name = driver.givenName,
                    surname = driver.familyName,
                    team = constructorName,
                    position = position,
                    points = points,
                    picture_url = imageUrl ?: "",
                    driverId = driver.driverId
                )
            )
        }

        prefs.edit().putString("driver_image_cache", gson.toJson(imageCache)).apply()
        results
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
