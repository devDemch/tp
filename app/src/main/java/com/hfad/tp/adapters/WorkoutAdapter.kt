package com.hfad.tp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hfad.tp.R
import com.hfad.tp.data.Workout

// Класс адаптера для списка тренировок, наследующий RecyclerView.Adapter
class WorkoutAdapter(private val workouts: MutableList<Workout>) :
    RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

    // Вложенный класс для держателя представления элемента списка
    class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Инициализация текстового поля для имени тренировки
        val workoutNameTextView: TextView = itemView.findViewById(R.id.workout_name_text_view)

        // Инициализация текстового поля для упражнений в тренировке
        val workoutExercisesTextView: TextView = itemView.findViewById(R.id.workout_exercises_text_view)
    }

    // Метод создания нового держателя представления для элемента списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        // Создание нового представления элемента списка из макета workout_item
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.workout_item, parent, false)

        // Возвращение нового держателя представления
        return WorkoutViewHolder(itemView)
    }

    // Метод привязки данных к держателю представления
    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        // Получение текущей тренировки по позиции в списке
        val workout = workouts[position]

        // Установка имени тренировки в соответствующее текстовое поле
        holder.workoutNameTextView.text = workout.name

        // Формирование строки с упражнениями тренировки
        val exercisesText = workout.exercises.joinToString("\n") {
            // Форматирование строки для каждого упражнения
            "${it.exercise.name} - ${it.weight}kg x ${it.repetitions} reps x ${it.sets} sets"
        }

        // Установка строки с упражнениями в соответствующее текстовое поле
        holder.workoutExercisesTextView.text = exercisesText
    }

    // Метод возвращающий количество элементов в списке
    override fun getItemCount() = workouts.size

    // Метод обновления данных в адаптере
    fun updateData(newWorkouts: List<Workout>) {
        // Очистка текущего списка тренировок
        workouts.clear()

        // Добавление новых тренировок в список
        workouts.addAll(newWorkouts)

        // Уведомление адаптера о необходимости перерисовать список
        notifyDataSetChanged()
    }
}
