package com.example.f1info.models

import java.io.Serializable

data class CircuitDescription(
    val main: String,
    val charakterystyka: String,
    val wyzwania: String,
    val ciekawostka: String,
    val drs: String
) : Serializable
