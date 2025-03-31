package com.example.f1info

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.f1info.fragments.CalendarFragment

class CalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerCalendar, CalendarFragment())
            .commit()
    }
}
