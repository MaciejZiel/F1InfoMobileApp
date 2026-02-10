package com.example.f1info.models

import java.io.Serializable

data class CircuitDescription(
    val main: String,
    val characteristics: String,
    val challenges: String,
    val trivia: String,
    val drs: String
) : Serializable
