package com.hfad.tp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hfad.tp.R
import com.hfad.tp.data.MuscleGroup

// Определение класса адаптера для RecyclerView
class MuscleGroupAdapter(
    // Список групп мышц, которые будут отображаться
    private var muscleGroups: MutableList<MuscleGroup>,
    // Callback-функция для обработки клика на элементе списка
    private val onItemClick: (MuscleGroup) -> Unit
) : RecyclerView.Adapter<MuscleGroupAdapter.MuscleGroupViewHolder>() {

    // Внутренний класс для представления одного элемента списка
    class MuscleGroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Получение ссылки на TextView для отображения названия группы мышц
        val muscleGroupNameTextView: TextView = itemView.findViewById(R.id.muscle_group_name)
        // Получение ссылки на TextView для отображения количества упражнений в группе
        val muscleGroupExerciseCountTextView: TextView = itemView.findViewById(R.id.muscle_group_exercise_count_text_view)
        // Получение ссылки на ImageView для отображения иконки группы мышц
        val muscleGroupImageView: ImageView = itemView.findViewById(R.id.muscle_group_image)
    }

    // Метод для создания нового представления элемента списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MuscleGroupViewHolder {
        // Создание макета для одного элемента списка
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.muscle_group_item, parent, false)
        // Возвращение нового объекта MuscleGroupViewHolder
        return MuscleGroupViewHolder(itemView)
    }

    // Метод для привязки данных к представлению элемента списка
    override fun onBindViewHolder(holder: MuscleGroupViewHolder, position: Int) {
        // Получение группы мышц по текущей позиции
        val muscleGroup = muscleGroups[position]
        // Установка текста названия группы мышц в TextView
        holder.muscleGroupNameTextView.text = muscleGroup.name
        // Установка текста количества упражнений в группе в TextView
        holder.muscleGroupExerciseCountTextView.text = "${muscleGroup.exerciseCount} упражнений"
        // Установка изображения группы мышц в ImageView
        holder.muscleGroupImageView.setImageResource(muscleGroup.imageResId)

        // Установка обработчика клика на элементе списка
        holder.itemView.setOnClickListener {
            // Вызов callback-функции при клике на элементе
            onItemClick(muscleGroup)
        }
    }

    // Метод для получения количества элементов в списке
    override fun getItemCount() = muscleGroups.size

    // Метод для обновления данных в адаптере
    fun updateData(newMuscleGroups: List<MuscleGroup>) {
        // Очистка текущего списка групп мышц
        muscleGroups.clear()
        // Добавление новых групп мышц в список
        muscleGroups.addAll(newMuscleGroups)
        // Уведомление адаптера об изменениях данных
        notifyDataSetChanged()
    }
}
