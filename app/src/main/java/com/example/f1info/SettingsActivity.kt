package com.example.f1info

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.f1info.databinding.ActivitySettingsBinding
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("F1_PREFS", MODE_PRIVATE)  // 👈 to musi być najpierw!

        // 🌗 Tryb ciemny
        val nightModeOn = prefs.getBoolean("dark_mode", false)
        binding.switchDarkMode.isChecked = nightModeOn

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("dark_mode", isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        // 🔧 Ulubiony zespół
        val savedTeam = prefs.getString("favorite_team", "Brak") ?: "Brak"
        val teams = listOf(
            "Brak",
            "Red Bull",
            "Ferrari",
            "Mercedes",
            "McLaren",
            "Aston Martin",
            "Alpine",
            "Williams",
            "Kick Sauber",
            "RB",
            "Haas"
        )

        val teamAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, teams)
        teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.teamSelector.adapter = teamAdapter
        binding.teamSelector.setSelection(teams.indexOf(savedTeam))

        binding.teamSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedTeam = parent?.getItemAtPosition(position).toString()
                prefs.edit().putString("favorite_team", selectedTeam).apply()
                Toast.makeText(this@SettingsActivity, "Ulubiony zespół: $selectedTeam", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // 📅 Sezon
        val savedSeason = prefs.getString("selected_season", "2023") ?: "2023"
        val seasons = listOf("2023", "2024", "2025")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, seasons)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.seasonSelector.adapter = adapter

        val selectedIndex = seasons.indexOf(savedSeason)
        if (selectedIndex >= 0) {
            binding.seasonSelector.setSelection(selectedIndex)
        }

        binding.seasonSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedSeason = parent?.getItemAtPosition(position).toString()
                prefs.edit().putString("selected_season", selectedSeason).apply()
                Toast.makeText(this@SettingsActivity, "Zapisano sezon: $selectedSeason", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
