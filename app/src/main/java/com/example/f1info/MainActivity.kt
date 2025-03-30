// MainActivity.kt
package com.example.f1info

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.f1info.databinding.ActivityMainBinding
import com.example.f1info.fragments.DriverStandingsFragment
import com.example.f1info.fragments.RaceInfoFragment
import com.example.f1info.fragments.TeamStandingsFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
    }

    private fun setupViewPager() {
        val pagerAdapter = F1PagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        // Connect TabLayout with ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Wyścig"
                1 -> "Kierowcy"
                2 -> "Zespoły"
                else -> "Zakładka"
            }
        }.attach()
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