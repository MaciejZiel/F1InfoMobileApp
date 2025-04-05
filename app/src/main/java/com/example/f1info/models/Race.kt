package com.example.f1info.models

data class Race(
    val sessionName: String,
    val country: String,
    val date: String,
    val circuitId: String,
    val meetingKey: Int
)
