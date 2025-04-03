package com.example.f1info

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.f1info.databinding.ActivityMainBinding
import com.example.f1info.fragments.DriverStandingsFragment
import com.example.f1info.fragments.RaceInfoFragment
import com.example.f1info.fragments.TeamStandingsFragment
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 🌗 Ustaw tryb ciemny na podstawie zapisanej preferencji
        val prefs = getSharedPreferences("F1_PREFS", MODE_PRIVATE)
        val darkMode = prefs.getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (darkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )


        // 🔔 Tworzenie kanału powiadomień
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "f1_channel",
                "Powiadomienia F1",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Powiadomienia o nadchodzących wyścigach"
            }

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        // 📛 Android 13+ – zapytaj o zgodę na powiadomienia
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

        // 🧱 UI startowy
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyTeamTheme()

        // Domyślnie ładujemy fragment "Wyścig"
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, RaceInfoFragment())
            .commit()

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_race -> {
                    loadFragment(RaceInfoFragment())
                    true
                }
                R.id.nav_drivers -> {
                    loadFragment(DriverStandingsFragment())
                    true
                }
                R.id.nav_teams -> {
                    loadFragment(TeamStandingsFragment())
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

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }


    fun applyTeamTheme() {
        val prefs = getSharedPreferences("F1_PREFS", MODE_PRIVATE)
        val team = prefs.getString("favorite_team", "Brak") ?: "Brak"

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
            "Haas" -> R.color.haas_red
            else -> R.color.default_primary
        }


        val newColor = ContextCompat.getColor(this, colorRes)

        // 🟣 Animowane przejście koloru
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
    }



    override fun onResume() {
        super.onResume()
        applyTeamTheme() // 🔁 ponownie zastosuj motyw po powrocie
    }

    private inner class F1PagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount() = 3
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> RaceInfoFragment()
                1 -> DriverStandingsFragment()
                2 -> TeamStandingsFragment()
                else -> RaceInfoFragment()
            }
        }
    }
}
