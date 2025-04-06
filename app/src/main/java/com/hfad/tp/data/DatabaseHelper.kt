package com.hfad.tp.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "workouts.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        // Создание таблицы для тренировок
        val CREATE_WORKOUT_TABLE = """
            CREATE TABLE IF NOT EXISTS workouts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                date TEXT NOT NULL,
                name TEXT NOT NULL
            );
        """.trimIndent()
        db.execSQL(CREATE_WORKOUT_TABLE)

        // Создание таблицы для наборов упражнений
        val CREATE_EXERCISE_SET_TABLE = """
            CREATE TABLE IF NOT EXISTS exercise_sets (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                workout_id INTEGER NOT NULL,
                exercise_name TEXT NOT NULL,
                weight REAL NOT NULL,
                repetitions INTEGER NOT NULL,
                sets INTEGER NOT NULL,
                FOREIGN KEY (workout_id) REFERENCES workouts(id)
            );
        """.trimIndent()
        db.execSQL(CREATE_EXERCISE_SET_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Обновление базы данных
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS workouts")
            db.execSQL("DROP TABLE IF EXISTS exercise_sets")
            onCreate(db)
        }
    }
}
