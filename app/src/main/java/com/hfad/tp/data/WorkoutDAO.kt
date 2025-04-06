package com.hfad.tp.data

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WorkoutDAO(private val dbHelper: DatabaseHelper) {

    // Метод добавления тренировки в базу данных
    fun addWorkout(workout: Workout) {
        val db = dbHelper.writableDatabase

        try {
            // Добавление тренировки
            val workoutValues = ContentValues()
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            workoutValues.put("date", dateFormat.format(workout.date))
            workoutValues.put("name", workout.name)
            val workoutId = db.insert("workouts", null, workoutValues).toInt()

            // Добавление наборов упражнений
            for (exerciseSet in workout.exercises) {
                val exerciseSetValues = ContentValues()
                exerciseSetValues.put("workout_id", workoutId)
                exerciseSetValues.put("exercise_name", exerciseSet.exercise.name)
                exerciseSetValues.put("weight", exerciseSet.weight)
                exerciseSetValues.put("repetitions", exerciseSet.repetitions)
                exerciseSetValues.put("sets", exerciseSet.sets)
                db.insert("exercise_sets", null, exerciseSetValues)
            }
        } catch (e: Exception) {
            // Логирование ошибок
            android.util.Log.e("WorkoutDAO", "Ошибка при добавлении тренировки", e)
        } finally {
            db.close()
        }
    }

    // Метод чтения тренировок из базы данных для определенной даты
    fun getWorkoutsForDate(date: Date): List<Workout> {
        val db = dbHelper.readableDatabase
        val workouts = mutableListOf<Workout>()

        try {
            // Формирование запроса для чтения тренировок по дате
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val query = """
                SELECT w.id, w.date, w.name
                FROM workouts w
                WHERE w.date = ?
            """.trimIndent()

            // Выполнение запроса
            val cursor = db.rawQuery(query, arrayOf(dateFormat.format(date)))

            while (cursor.moveToNext()) {
                val workoutId = cursor.getInt(0)
                val workoutDate = cursor.getString(1)
                val workoutName = cursor.getString(2)

                // Чтение наборов упражнений для тренировки
                val exerciseSets = getExerciseSetsForWorkout(workoutId)

                // Создание объекта тренировки
                val workout = Workout(
                    SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(workoutDate)!!,
                    workoutName,
                    exerciseSets
                )

                workouts.add(workout)
            }
        } catch (e: Exception) {
            // Логирование ошибок
            android.util.Log.e("WorkoutDAO", "Ошибка при чтении тренировок", e)
        } finally {
            db.close()
        }

        return workouts
    }

    // Метод чтения наборов упражнений для определенной тренировки
    private fun getExerciseSetsForWorkout(workoutId: Int): List<ExerciseSet> {
        val db = dbHelper.readableDatabase
        val exerciseSets = mutableListOf<ExerciseSet>()

        try {
            // Формирование запроса для чтения наборов упражнений
            val query = """
                SELECT exercise_name, weight, repetitions, sets
                FROM exercise_sets
                WHERE workout_id = ?
            """.trimIndent()

            // Выполнение запроса
            val cursor = db.rawQuery(query, arrayOf(workoutId.toString()))

            while (cursor.moveToNext()) {
                val exerciseName = cursor.getString(0)
                val weight = cursor.getDouble(1)
                val repetitions = cursor.getInt(2)
                val sets = cursor.getInt(3)

                // Создание объекта упражнения
                val exercise = Exercise(exerciseName, "")

                // Создание набора упражнений
                val exerciseSet = ExerciseSet(exercise, weight, repetitions, sets)

                exerciseSets.add(exerciseSet)
            }
        } catch (e: Exception) {
            // Логирование ошибок
            android.util.Log.e("WorkoutDAO", "Ошибка при чтении наборов упражнений", e)
        } finally {
            db.close()
        }

        return exerciseSets
    }
}
