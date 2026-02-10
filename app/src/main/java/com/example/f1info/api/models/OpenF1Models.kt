package com.example.f1info.api.models

data class OpenF1Session(
    val session_key: Int,
    val session_name: String,
    val session_type: String,
    val date_start: String,
    val date_end: String? = null,
    val meeting_key: Int? = null,
    val meeting_name: String? = null,
    val circuit_short_name: String? = null,
    val location: String? = null,
    val country_name: String? = null,
    val year: Int? = null
)

data class OpenF1Driver(
    val driver_number: Int,
    val first_name: String? = null,
    val last_name: String? = null,
    val full_name: String? = null,
    val team_name: String? = null,
    val headshot_url: String? = null
)

data class OpenF1ChampionshipDriver(
    val driver_number: Int,
    val position_current: Int,
    val points_current: Double?
)

data class OpenF1ChampionshipTeam(
    val team_name: String,
    val position_current: Int,
    val points_current: Double?
)

data class OpenF1SessionResult(
    val driver_number: Int,
    val position: Int
)
