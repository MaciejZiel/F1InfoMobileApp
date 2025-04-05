package com.example.f1info.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.f1info.adapters.TeamStandingsAdapter
import com.example.f1info.api.StandingsApiService
import com.example.f1info.api.StandingsApiClient
import com.example.f1info.databinding.FragmentTeamStandingsBinding
import com.example.f1info.models.ConstructorStanding
import com.example.f1info.models.DriverStanding
import kotlinx.coroutines.launch

class TeamStandingsFragment : Fragment() {
    private var _binding: FragmentTeamStandingsBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding is null")
    private lateinit var apiService: StandingsApiService
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
        apiService = StandingsApiClient.getInstance().create(StandingsApiService::class.java)
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

    private fun calculateConstructorStandings(driverStandings: List<DriverStanding>): List<ConstructorStanding> {
        val constructorPointsMap = mutableMapOf<String, Double>()

        for (driver in driverStandings) {
            val constructorName = driver.team
            val driverPoints = driver.points
            constructorPointsMap[constructorName] = constructorPointsMap.getOrDefault(constructorName, 0.0) + driverPoints
        }

        val constructorStandings = constructorPointsMap.map { entry ->
            ConstructorStanding(
                constructor_name = entry.key,
                position = 0,
                points = entry.value
            )
        }

        return constructorStandings
            .sortedByDescending { it.points }
            .mapIndexed { index, constructor ->
                constructor.copy(position = index + 1)
            }
    }

    private fun loadTeamStandings() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                _binding?.progressBar?.visibility = View.VISIBLE
                val driverStandings = apiService.getLiveDriverStandings()
                val constructorStandings = calculateConstructorStandings(driverStandings)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
