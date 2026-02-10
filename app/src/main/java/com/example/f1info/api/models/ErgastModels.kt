package com.example.f1info.api.models

data class ErgastDriverStandingsResponse(
    val MRData: ErgastDriverStandingsMRData
)

data class ErgastDriverStandingsMRData(
    val StandingsTable: ErgastDriverStandingsTable
)

data class ErgastDriverStandingsTable(
    val StandingsLists: List<ErgastDriverStandingsList>
)

data class ErgastDriverStandingsList(
    val DriverStandings: List<ErgastDriverStanding>
)

data class ErgastDriverStanding(
    val position: String,
    val points: String,
    val Driver: ErgastDriver,
    val Constructors: List<ErgastConstructor>
)

data class ErgastConstructorStandingsResponse(
    val MRData: ErgastConstructorStandingsMRData
)

data class ErgastConstructorStandingsMRData(
    val StandingsTable: ErgastConstructorStandingsTable
)

data class ErgastConstructorStandingsTable(
    val StandingsLists: List<ErgastConstructorStandingsList>
)

data class ErgastConstructorStandingsList(
    val ConstructorStandings: List<ErgastConstructorStanding>
)

data class ErgastConstructorStanding(
    val position: String,
    val points: String,
    val Constructor: ErgastConstructor
)

data class ErgastRaceResultsResponse(
    val MRData: ErgastRaceMRData
)

data class ErgastRaceMRData(
    val RaceTable: ErgastRaceTable
)

data class ErgastRaceTable(
    val Races: List<ErgastRace>
)

data class ErgastRace(
    val raceName: String,
    val date: String,
    val time: String?,
    val Circuit: ErgastCircuit,
    val Results: List<ErgastResult>
)

data class ErgastCircuit(
    val circuitId: String,
    val circuitName: String,
    val Location: ErgastLocation
)

data class ErgastLocation(
    val country: String
)

data class ErgastResult(
    val position: String,
    val Driver: ErgastDriver,
    val Constructor: ErgastConstructor
)

data class ErgastDriver(
    val driverId: String,
    val givenName: String,
    val familyName: String,
    val url: String?
)

data class ErgastConstructor(
    val name: String
)
