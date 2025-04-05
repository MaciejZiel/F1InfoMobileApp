package com.example.f1info.fragments

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
import com.example.f1info.api.StandingsApiClient
import com.example.f1info.api.StandingsApiService
import com.example.f1info.databinding.FragmentDriverStandingsBinding
import kotlinx.coroutines.launch

class DriverStandingsFragment : Fragment() {

    private var _binding: FragmentDriverStandingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DriverStandingsAdapter
    private lateinit var apiService: StandingsApiService



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDriverStandingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        apiService = StandingsApiClient.getInstance().create(StandingsApiService::class.java) as StandingsApiService
        adapter = DriverStandingsAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
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

    private fun loadDriverStandings() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                _binding?.progressBar?.visibility = View.VISIBLE

                val standings = apiService.getLiveDriverStandings()

                _binding?.let { binding ->
                    if (standings.isNotEmpty()) {
                        adapter.submitList(standings)
                        binding.tvNoData.visibility = View.GONE
                    } else {
                        binding.tvNoData.visibility = View.VISIBLE
                    }
                }

            } catch (e: Exception) {
                Log.e("DriverStandingsFragment", "Błąd podczas ładowania danych", e)
                _binding?.let {
                    Toast.makeText(it.root.context, "Błąd: ${e.message}", Toast.LENGTH_SHORT).show()
                    it.tvNoData.visibility = View.VISIBLE
                }
            } finally {
                _binding?.let {
                    it.progressBar.visibility = View.GONE
                    it.swipeRefresh.isRefreshing = false
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
