package com.example.f1info

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

class F1InfoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val prefs = getSharedPreferences("F1_PREFS", MODE_PRIVATE)
        val languageTag = prefs.getString("app_language", "en") ?: "en"
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageTag))
    }
}
