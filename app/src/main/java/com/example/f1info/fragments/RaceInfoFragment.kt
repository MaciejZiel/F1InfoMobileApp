package com.example.f1info.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.f1info.api.OpenF1ApiService
import com.example.f1info.api.OpenF1Client
import com.example.f1info.databinding.FragmentRaceInfoBinding
import com.example.f1info.models.Session
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RaceInfoFragment : Fragment() {
    private var _binding: FragmentRaceInfoBinding? = null
    private lateinit var apiService: OpenF1ApiService

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
        apiService = OpenF1Client.getInstance().create(OpenF1ApiService::class.java)
        _binding?.swipeRefresh?.setOnRefreshListener {
            loadCurrentRaceInfo()
        }
        loadCurrentRaceInfo()
    }

    private fun loadCurrentRaceInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                _binding?.progressBar?.visibility = View.VISIBLE

                val sessions = apiService.getSessions()
                val currentSession = sessions.firstOrNull {
                    val sessionDate = it.session_start_date
                    val now = System.currentTimeMillis() / 1000
                    sessionDate > now - 86400
                }

                if (currentSession != null) {
                    updateUI(currentSession)
                } else {
                    _binding?.tvNoData?.visibility = View.VISIBLE
                    _binding?.cardRaceInfo?.visibility = View.GONE
                }

            } catch (e: Exception) {
                if (e.message != "Job was cancelled") {
                    context?.let {
                        Toast.makeText(it, "Błąd: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } finally {
                _binding?.progressBar?.visibility = View.GONE
                _binding?.swipeRefresh?.isRefreshing = false
            }
        }
    }

    private fun updateUI(session: Session) {
        _binding?.apply {
            tvNoData.visibility = View.GONE
            cardRaceInfo.visibility = View.VISIBLE
            tvSessionName.text = session.session_name
            tvTrackName.text = session.meeting_name
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getDefault()
            val date = Date(session.session_start_date * 1000)
            tvDate.text = dateFormat.format(date)
        }
        loadSessionResults(session.session_key)
    }

    private fun loadSessionResults(sessionKey: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val results = apiService.getSessionResults(sessionKey)
                _binding?.tvTopThree?.text = if (results.isNotEmpty()) {
                    buildString {
                        append("TOP 3:\n")
                        results.sortedBy { it.position }.take(3).forEach { driver ->
                            append("${driver.position}. ${driver.driver_full_name} (${driver.constructor_name})\n")
                        }
                    }
                } else {
                    "Wyniki jeszcze niedostępne"
                }

            } catch (_: Exception) {
                _binding?.tvTopThree?.text = "Nie udało się załadować wyników"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
