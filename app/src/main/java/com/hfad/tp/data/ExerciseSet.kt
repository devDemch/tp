package com.hfad.tp.data

import java.util.Date

data class ExerciseSet(
    val exercise: Exercise, // Упражнение
    val weight: Double, // Вес
    val repetitions: Int, // Количество повторений
    val sets: Int, // Количество подходов
    val date: Date? = null // Дата выполнения подхода
)
