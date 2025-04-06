package com.hfad.tp.data

import java.util.Date

data class Workout(
    val date: Date,
    val name: String,
    val exercises: List<ExerciseSet>
)
