
// Result.kt
package com.example.f1info.models

data class Result(
    val driver_number: Int,
    val driver_full_name: String,
    val constructor_name: String,
    val position: Int,
    val lap_time: String? = null,
    val gap_to_leader: String? = null,
    val grid_position: Int? = null
)
