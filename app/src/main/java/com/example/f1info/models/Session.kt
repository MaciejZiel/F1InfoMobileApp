// Session.kt
package com.example.f1info.models

data class Session(
    val session_key: String,
    val session_name: String,
    val session_type: String,
    val meeting_name: String,
    val meeting_key: String,
    val circuit_key: String,
    val circuit_short_name: String,
    val session_start_date: Long,
    val session_end_date: Long
)