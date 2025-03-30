// DriverStanding.kt
package com.example.f1info.models

data class DriverStanding(
    val driver_number: Int,
    val driver_full_name: String,
    val driver_name_acronym: String,
    val constructor_name: String,
    val position: Int,
    val points: Double
)