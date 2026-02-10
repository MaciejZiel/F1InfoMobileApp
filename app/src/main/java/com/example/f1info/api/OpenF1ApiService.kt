package com.example.f1info.api

import com.example.f1info.api.models.OpenF1ChampionshipDriver
import com.example.f1info.api.models.OpenF1ChampionshipTeam
import com.example.f1info.api.models.OpenF1Driver
import com.example.f1info.api.models.OpenF1Session
import com.example.f1info.api.models.OpenF1SessionResult
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenF1ApiService {
    @GET("sessions")
    suspend fun getSessions(
        @Query("year") year: Int? = null,
        @Query("session_type") sessionType: String? = null
    ): List<OpenF1Session>

    @GET("drivers")
    suspend fun getDrivers(
        @Query("session_key") sessionKey: Int
    ): List<OpenF1Driver>

    @GET("championship_drivers")
    suspend fun getChampionshipDrivers(
        @Query("session_key") sessionKey: Int
    ): List<OpenF1ChampionshipDriver>

    @GET("championship_teams")
    suspend fun getChampionshipTeams(
        @Query("session_key") sessionKey: Int
    ): List<OpenF1ChampionshipTeam>

    @GET("session_result")
    suspend fun getSessionResult(
        @Query("session_key") sessionKey: Int,
        @Query("position") position: String? = null
    ): List<OpenF1SessionResult>
}
