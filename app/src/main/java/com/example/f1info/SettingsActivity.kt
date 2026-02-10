package com.example.f1info

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.f1info.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    @SuppressLint("UseKtx")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val prefs = getSharedPreferences("F1_PREFS", MODE_PRIVATE)
        val nightModeOn = prefs.getBoolean("dark_mode", false)
        binding.switchDarkMode.isChecked = nightModeOn

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("dark_mode", isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        val savedTeam = prefs.getString("favorite_team", "None") ?: "None"
        val teams = listOf(
            "F1",
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
            @SuppressLint("ShowToast")
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedTeam = parent?.getItemAtPosition(position).toString()
                prefs.edit().putString("favorite_team", selectedTeam).apply()
                Toast.makeText(
                    this@SettingsActivity,
                    getString(R.string.favorite_team_toast, selectedTeam),
                    Toast.LENGTH_SHORT
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val savedSeason = prefs.getString("selected_season", "2026") ?: "2026"
        val seasons = (1950..2026).map { it.toString() }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, seasons)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.seasonSelector.adapter = adapter

        val selectedIndex = seasons.indexOf(savedSeason)
        if (selectedIndex >= 0) {
            binding.seasonSelector.setSelection(selectedIndex)
        }

        binding.seasonSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("ShowToast")
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedSeason = parent?.getItemAtPosition(position).toString()
                prefs.edit().putString("selected_season", selectedSeason).apply()
                Toast.makeText(
                    this@SettingsActivity,
                    getString(R.string.saved_season_toast, selectedSeason),
                    Toast.LENGTH_SHORT
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val languageTags = listOf("en", "pl", "es")
        val languageLabels = listOf(
            getString(R.string.language_english),
            getString(R.string.language_polish),
            getString(R.string.language_spanish)
        )
        val languageAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languageLabels)
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.languageSelector.adapter = languageAdapter

        val savedLanguage = prefs.getString("app_language", "en") ?: "en"
        val languageIndex = languageTags.indexOf(savedLanguage).takeIf { it >= 0 } ?: 0
        binding.languageSelector.setSelection(languageIndex)

        binding.languageSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedTag = languageTags[position]
                if (selectedTag != savedLanguage) {
                    prefs.edit().putString("app_language", selectedTag).apply()
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(selectedTag))
                    recreate()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
