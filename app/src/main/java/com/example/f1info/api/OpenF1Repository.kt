package com.example.f1info.api

import com.example.f1info.api.models.OpenF1Session
import java.time.OffsetDateTime
import java.time.ZoneOffset

object OpenF1Repository {
    fun parseDateStart(dateStart: String): OffsetDateTime? {
        return try {
            OffsetDateTime.parse(dateStart)
        } catch (_: Exception) {
            null
        }
    }

    suspend fun getLatestRaceSession(api: OpenF1ApiService, year: Int): OpenF1Session? {
        val sessions = api.getSessions(year = year, sessionType = "Race")
        if (sessions.isEmpty()) return null

        val now = OffsetDateTime.now(ZoneOffset.UTC)
        val pastSessions = sessions.filter { session ->
            val start = parseDateStart(session.date_start)
            start != null && (start.isBefore(now) || start.isEqual(now))
        }

        return (pastSessions.maxByOrNull { parseDateStart(it.date_start) ?: OffsetDateTime.MIN }
            ?: sessions.maxByOrNull { parseDateStart(it.date_start) ?: OffsetDateTime.MIN })
    }
}
