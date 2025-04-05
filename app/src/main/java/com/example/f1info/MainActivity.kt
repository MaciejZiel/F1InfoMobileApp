package com.example.f1info

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.f1info.databinding.ActivityMainBinding
import com.example.f1info.fragments.DriverStandingsFragment
import com.example.f1info.fragments.RaceInfoFragment
import com.example.f1info.fragments.TeamStandingsFragment
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("F1_PREFS", MODE_PRIVATE)
        val darkMode = prefs.getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (darkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        val channel = NotificationChannel(
            "f1_channel",
            "Powiadomienia F1",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Powiadomienia o nadchodzących wyścigach"
        }

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyTeamTheme()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, RaceInfoFragment())
            .commit()

        binding.toolbar.title = "Informacje o wyścigu"

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_race -> {
                    loadFragment(RaceInfoFragment(), "Informacje o wyścigu")
                    true
                }
                R.id.nav_drivers -> {
                    loadFragment(DriverStandingsFragment(), "Klasyfikacja kierowców")
                    true
                }
                R.id.nav_teams -> {
                    loadFragment(TeamStandingsFragment(), "Klasyfikacja konstruktorów")
                    true
                }
                R.id.nav_calendar -> {
                    startActivity(Intent(this, CalendarActivity::class.java))
                    true
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment, title: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()

        binding.toolbar.title = title
    }

    fun applyTeamTheme() {
        val prefs = getSharedPreferences("F1_PREFS", MODE_PRIVATE)
        val team = prefs.getString("favorite_team", "Brak") ?: "Brak"

        val logoRes = when (team) {
            "Red Bull" -> R.drawable.redbull_logo
            "Ferrari" -> R.drawable.ferrari_logo
            "Mercedes" -> R.drawable.mercedes_logo
            "McLaren" -> R.drawable.mclaren_logo
            "Aston Martin" -> R.drawable.aston_martin_logo
            "Alpine" -> R.drawable.alpine_logo
            "Williams" -> R.drawable.williams_logo
            "Kick Sauber" -> R.drawable.kick_sauber_logo
            "RB" -> R.drawable.rb_logo
            "Haas" -> R.drawable.haas_logo
            else -> R.drawable.f1_logo
        }


        val teamLogo = findViewById<ImageView>(R.id.teamLogoBackground)
        teamLogo.setImageResource(logoRes)

        val colorRes = when (team) {
            "Red Bull" -> R.color.redbull_blue
            "Ferrari" -> R.color.ferrari_red
            "Mercedes" -> R.color.mercedes_teal
            "McLaren" -> R.color.mclaren_orange
            "Aston Martin" -> R.color.aston_green
            "Alpine" -> R.color.alpine_blue
            "Williams" -> R.color.williams_blue
            "Kick Sauber" -> R.color.sauber_green
            "RB" -> R.color.rb_violet
            "Haas" -> R.color.haas_gray
            else -> R.color.default_primary
        }

        val newColor = ContextCompat.getColor(this, colorRes)
        val oldColor = (binding.toolbar.background as? ColorDrawable)?.color ?: newColor
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), oldColor, newColor)
        colorAnimation.duration = 300
        colorAnimation.addUpdateListener { animator ->
            val animatedColor = animator.animatedValue as Int
            binding.toolbar.setBackgroundColor(animatedColor)
            binding.bottomNavigationView.setBackgroundColor(animatedColor)
        }
        colorAnimation.start()
        window.statusBarColor = newColor

        binding.bottomNavigationView.itemIconTintList = ContextCompat.getColorStateList(this, R.color.nav_icon_color)
        binding.bottomNavigationView.itemTextColor = ContextCompat.getColorStateList(this, R.color.nav_icon_color)
    }


    override fun onResume() {
        super.onResume()
        applyTeamTheme()
    }
}
