package com.example.f1info.api

import com.example.f1info.models.Result
import com.example.f1info.models.Session
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenF1ApiService {
    @GET("sessions")
    suspend fun getSessions(): List<Session>

    @GET("results")
    suspend fun getSessionResults(@Query("session_key") sessionKey: String): List<Result>
}
