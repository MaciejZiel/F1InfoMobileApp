
// F1ApiService.kt
package com.example.f1info.api

import com.example.f1info.models.ConstructorStanding
import com.example.f1info.models.DriverStanding
import com.example.f1info.models.Result
import com.example.f1info.models.Session
import retrofit2.http.GET
import retrofit2.http.Query

interface F1ApiService {

    @GET("sessions")
    suspend fun getSessions(): List<Session>

    @GET("driver_standings")
    suspend fun getDriverStandings(): List<DriverStanding>

    @GET("constructor_standings")
    suspend fun getConstructorStandings(): List<ConstructorStanding>

    @GET("results")
    suspend fun getSessionResults(@Query("session_key") sessionKey: String): List<Result>
}
