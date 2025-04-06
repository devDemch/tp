package com.hfad.tp.ui.journal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hfad.tp.data.DatabaseHelper
import com.hfad.tp.data.Workout
import com.hfad.tp.data.WorkoutDAO
import com.hfad.tp.data.ExerciseSet
import com.hfad.tp.data.Exercise
import java.text.SimpleDateFormat
import java.util.*

class JournalViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = DatabaseHelper(application)
    private val workoutDAO = WorkoutDAO(dbHelper)

    // LiveData для хранения тренировок
    private val _workouts = MutableLiveData<List<Workout>>()
    val workouts: LiveData<List<Workout>> = _workouts

    // LiveData для хранения статистики тренировок
    private val _statistics = MutableLiveData<String>().apply {
        value = "Нет статистики для выбранной даты"
    }
    val statistics: LiveData<String> = _statistics

    // Метод загрузки тренировок для выбранной даты
    fun loadWorkoutsForDate(date: Date) {
        try {
            val workoutsForDate = workoutDAO.getWorkoutsForDate(date)
            _workouts.postValue(workoutsForDate)

            // Обновление статистики
            calculateStatistics(workoutsForDate)
        } catch (e: Exception) {
            // Логирование ошибок
            android.util.Log.e("JournalViewModel", "Ошибка при загрузке тренировок", e)
        }
    }

    // Метод сохранения тренировки
    fun saveWorkout(workout: Workout) {
        workoutDAO.addWorkout(workout)
    }

    // Метод расчета статистики по тренировкам
    private fun calculateStatistics(workouts: List<Workout>) {
        if (workouts.isEmpty()) {
            _statistics.value = "Нет статистики для выбранной даты"
            return
        }

        // Расчет общего количества упражнений
        val totalExercises = workouts.sumOf { it.exercises.size }

        // Расчет общего количества подходов
        val totalSets = workouts.sumOf { workout -> workout.exercises.sumOf { it.sets } }

        // Расчет общего количества повторений
        val totalRepetitions = workouts.sumOf { workout -> workout.exercises.sumOf { it.repetitions } }

        // Формирование строки статистики
        val statisticsText = "Упражнений: $totalExercises, Подходов: $totalSets, Повторений: $totalRepetitions"
        _statistics.value = statisticsText
    }
}
