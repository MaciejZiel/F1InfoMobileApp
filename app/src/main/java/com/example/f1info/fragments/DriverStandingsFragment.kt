
// DriverStandingsFragment.kt
package com.example.f1info.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.f1info.adapters.DriverStandingsAdapter
import com.example.f1info.api.F1ApiService
import com.example.f1info.api.RetrofitClient
import com.example.f1info.databinding.FragmentDriverStandingsBinding
import kotlinx.coroutines.launch

class DriverStandingsFragment : Fragment() {

    private var _binding: FragmentDriverStandingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiService: F1ApiService
    private lateinit var adapter: DriverStandingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDriverStandingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiService = RetrofitClient.getInstance().create(F1ApiService::class.java)

        setupRecyclerView()

        binding.swipeRefresh.setOnRefreshListener {
            loadDriverStandings()
        }

        loadDriverStandings()
    }

    private fun setupRecyclerView() {
        adapter = DriverStandingsAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun loadDriverStandings() {
        lifecycleScope.launch {
            try {
                binding.progressBar.visibility = View.VISIBLE

                val standings = apiService.getDriverStandings()

                if (standings.isNotEmpty()) {
                    adapter.submitList(standings)
                    binding.tvNoData.visibility = View.GONE
                } else {
                    binding.tvNoData.visibility = View.VISIBLE
                }

            } catch (e: Exception) {
                Toast.makeText(context, "Błąd: ${e.message}", Toast.LENGTH_SHORT).show()
                binding.tvNoData.visibility = View.VISIBLE
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
