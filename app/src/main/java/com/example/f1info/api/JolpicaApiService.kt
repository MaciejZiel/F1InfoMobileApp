package com.example.f1info.api

import com.example.f1info.api.models.ErgastConstructorStandingsResponse
import com.example.f1info.api.models.ErgastDriverStandingsResponse
import com.example.f1info.api.models.ErgastRaceResultsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JolpicaApiService {
    @GET("{season}/driverStandings.json")
    suspend fun getDriverStandings(@Path("season") season: String): ErgastDriverStandingsResponse

    @GET("{season}/constructorStandings.json")
    suspend fun getConstructorStandings(@Path("season") season: String): ErgastConstructorStandingsResponse

    @GET("{season}/last/results.json")
    suspend fun getLastRaceResults(@Path("season") season: String): ErgastRaceResultsResponse

    @GET("{season}/drivers/{driverId}/results.json")
    suspend fun getDriverResults(
        @Path("season") season: String,
        @Path("driverId") driverId: String,
        @Query("limit") limit: Int = 500
    ): ErgastRaceResultsResponse
}
