package com.example.f1info.models

data class DriverStanding(
    val name: String,
    val surname: String,
    val team: String,
    val position: Int,
    val points: Int,
    val picture_url: String,
    val basePodiums: Int = getBasePodiums("$name $surname")
)

fun getBasePodiums(fullName: String): Int {
    return when (fullName.uppercase()) {
        "MAX VERSTAPPEN" -> 113
        "LEWIS HAMILTON" -> 197
        "CHARLES LECLERC" -> 30
        "FERNANDO ALONSO" -> 106
        "SERGIO PEREZ" -> 35
        "GEORGE RUSSELL" -> 11
        "CARLOS SAINZ" -> 18
        "LANDO NORRIS" -> 14
        "OSCAR PIASTRI" -> 2
        "ALEXANDER ALBON" -> 2
        "LANCE STROLL" -> 3
        "ESTEBAN OCON" -> 3
        "PIERRE GASLY" -> 4
        "YUKI TSUNODA" -> 0
        "VALTTERI BOTTAS" -> 67
        "ZHOUGUANYU" -> 0
        "NICO HULKENBERG" -> 0
        "KEVIN MAGNUSSEN" -> 1
        else -> 0
    }
}
