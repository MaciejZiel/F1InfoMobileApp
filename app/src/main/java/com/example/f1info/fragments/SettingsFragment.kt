package com.example.f1info.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.f1info.R

class SettingsFragment : Fragment() {

    private val availableSeasons = listOf("2025", "2024", "2023", "2022", "2021")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val spinner = view.findViewById<Spinner>(R.id.spinnerSeasons)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, availableSeasons)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedSeason = availableSeasons[position]
                Toast.makeText(requireContext(), "Wybrany sezon: $selectedSeason", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        return view
    }
}
