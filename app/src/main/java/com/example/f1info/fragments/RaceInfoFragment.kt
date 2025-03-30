
// RaceInfoFragment.kt
package com.example.f1info.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.f1info.api.F1ApiService
import com.example.f1info.api.RetrofitClient
import com.example.f1info.databinding.FragmentRaceInfoBinding
import com.example.f1info.models.Session
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class RaceInfoFragment : Fragment() {

    private var _binding: FragmentRaceInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiService: F1ApiService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRaceInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiService = RetrofitClient.getInstance().create(F1ApiService::class.java)

        binding.swipeRefresh.setOnRefreshListener {
            loadCurrentRaceInfo()
        }

        loadCurrentRaceInfo()
    }

    private fun loadCurrentRaceInfo() {
        lifecycleScope.launch {
            try {
                binding.progressBar.visibility = View.VISIBLE

                // Fetch current or next session
                val sessions = apiService.getSessions()
                val currentSession = sessions.firstOrNull {
                    val sessionDate = it.session_start_date
                    val now = System.currentTimeMillis() / 1000
                    sessionDate > now - 86400 // Show sessions from last 24 hours
                }

                if (currentSession != null) {
                    updateUI(currentSession)
                } else {
                    binding.tvNoData.visibility = View.VISIBLE
                    binding.cardRaceInfo.visibility = View.GONE
                }

            } catch (e: Exception) {
                Toast.makeText(context, "Błąd: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun updateUI(session: Session) {
        binding.tvNoData.visibility = View.GONE
        binding.cardRaceInfo.visibility = View.VISIBLE

        binding.tvSessionName.text = session.session_name
        binding.tvTrackName.text = session.meeting_name

        // Format date
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getDefault()
        val date = Date(session.session_start_date * 1000)
        binding.tvDate.text = dateFormat.format(date)

        // Load results if available
        loadSessionResults(session.session_key)
    }

    private fun loadSessionResults(sessionKey: String) {
        lifecycleScope.launch {
            try {
                val results = apiService.getSessionResults(sessionKey)

                if (results.isNotEmpty()) {
                    binding.tvTopThree.text = buildString {
                        append("TOP 3:\n")
                        results.sortedBy { it.position }.take(3).forEach { driver ->
                            append("${driver.position}. ${driver.driver_full_name} (${driver.constructor_name})\n")
                        }
                    }
                } else {
                    binding.tvTopThree.text = "Wyniki jeszcze niedostępne"
                }

            } catch (e: Exception) {
                binding.tvTopThree.text = "Nie udało się załadować wyników"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
