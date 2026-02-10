package com.example.f1info.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.f1info.adapters.TeamStandingsAdapter
import com.example.f1info.api.JolpicaApiService
import com.example.f1info.api.JolpicaClient
import com.example.f1info.api.OpenF1ApiService
import com.example.f1info.api.OpenF1Client
import com.example.f1info.api.OpenF1Repository
import com.example.f1info.databinding.FragmentTeamStandingsBinding
import com.example.f1info.models.ConstructorStanding
import kotlinx.coroutines.launch

class TeamStandingsFragment : Fragment() {
    private var _binding: FragmentTeamStandingsBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding is null")
    private lateinit var openF1Api: OpenF1ApiService
    private lateinit var jolpicaApi: JolpicaApiService
    private lateinit var adapter: TeamStandingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamStandingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openF1Api = OpenF1Client.getInstance().create(OpenF1ApiService::class.java)
        jolpicaApi = JolpicaClient.getInstance().create(JolpicaApiService::class.java)
        setupRecyclerView()
        binding.swipeRefresh.setOnRefreshListener {
            loadTeamStandings()
        }

        loadTeamStandings()
    }

    private fun setupRecyclerView() {
        adapter = TeamStandingsAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun loadTeamStandings() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                _binding?.progressBar?.visibility = View.VISIBLE
                val season = getSelectedSeason()
                val constructorStandings = if (season >= 2023) {
                    loadOpenF1ConstructorStandings(season)
                } else {
                    loadErgastConstructorStandings(season)
                }

                _binding?.let { binding ->
                    if (constructorStandings.isNotEmpty()) {
                        adapter.submitList(constructorStandings)
                        binding.tvNoData.visibility = View.GONE
                    } else {
                        binding.tvNoData.visibility = View.VISIBLE
                    }
                }

            } catch (_: Exception) {
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

    private suspend fun loadOpenF1ConstructorStandings(season: Int): List<ConstructorStanding> {
        val session = OpenF1Repository.getLatestRaceSession(openF1Api, season) ?: return emptyList()
        val sessionKey = session.session_key
        val standings = openF1Api.getChampionshipTeams(sessionKey)

        return standings
            .map {
                ConstructorStanding(
                    constructor_name = it.team_name,
                    position = it.position_current,
                    points = it.points_current ?: 0.0
                )
            }
            .sortedBy { it.position }
    }

    private suspend fun loadErgastConstructorStandings(season: Int): List<ConstructorStanding> {
        val response = jolpicaApi.getConstructorStandings(season.toString())
        val standingsList = response.MRData.StandingsTable.StandingsLists
            .firstOrNull()
            ?.ConstructorStandings
            ?: emptyList()

        return standingsList.map {
            ConstructorStanding(
                constructor_name = it.Constructor.name,
                position = it.position.toIntOrNull() ?: 0,
                points = it.points.toDoubleOrNull() ?: 0.0
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
