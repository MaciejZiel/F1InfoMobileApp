package com.example.f1info

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.f1info.fragments.CalendarFragment
import androidx.core.graphics.drawable.toDrawable

class CalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        val teamColor = intent.getIntExtra("team_color", ContextCompat.getColor(this, R.color.default_primary))
        supportActionBar?.setBackgroundDrawable(teamColor.toDrawable())
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerCalendar, CalendarFragment())
            .commit()
    }

}
