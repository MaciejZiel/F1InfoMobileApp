package com.example.f1info.api

import com.example.f1info.models.DriverStanding
import retrofit2.http.GET

interface StandingsApiService {
    @GET("drivers")
    suspend fun getLiveDriverStandings(): List<DriverStanding>
}
