package com.hfad.tp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hfad.tp.R
import com.hfad.tp.data.Exercise

// Определение класса адаптера для RecyclerView
class ExerciseAdapter(private var exercises: MutableList<Exercise>) :
    RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    // Внутренний класс для представления одного элемента списка
    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Получение ссылки на TextView для отображения названия упражнения
        val exerciseNameTextView: TextView = itemView.findViewById(R.id.exercise_name)
    }

    // Метод для создания нового представления элемента списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        // Создание макета для одного элемента списка
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.exercise_item, parent, false)
        // Возвращение нового объекта ExerciseViewHolder
        return ExerciseViewHolder(itemView)
    }

    // Метод для привязки данных к представлению элемента списка
    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        // Получение упражнения по текущей позиции
        val exercise = exercises[position]
        // Установка текста названия упражнения в TextView
        holder.exerciseNameTextView.text = exercise.name
    }

    // Метод для получения количества элементов в списке
    override fun getItemCount() = exercises.size

    // Метод для обновления данных в адаптере
    fun updateData(newExercises: List<Exercise>) {
        // Очистка текущего списка упражнений
        exercises.clear()
        // Добавление новых упражнений в список
        exercises.addAll(newExercises)
        // Уведомление адаптера об изменениях данных
        notifyDataSetChanged()
    }
}
