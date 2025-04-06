package com.hfad.tp.ui.exercise_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hfad.tp.data.Exercise

class ExerciseListViewModel : ViewModel() {

    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> = _exercises

    // Пример метода для загрузки упражнений
    fun loadExercises(muscleGroupName: String?) {
        // Имитация загрузки данных из базы данных или другого источника
        val allExercises = listOf(
            Exercise("Подтягивание в гравитроне обратный хват", "Спина"),
            Exercise("Подтягивание в гравитроне прямой хват", "Спина"),
            Exercise("Вертикальная тяга", "Спина"),
            Exercise("Горизонтальная тяга", "Спина"),
            Exercise("Тяга грифа в наклоне обратным хватом", "Спина"),
            Exercise("Гиперэкстензия", "Спина"),
            Exercise("Подтягивания", "Спина"),
            Exercise("Тяга штанги в наклоне", "Спина"),
            Exercise("Жим лежа", "Грудные мышцы"),
            Exercise("Бабочка", "Грудные мышцы"),
            Exercise("Отжимания", "Грудные мышцы"),
            Exercise("Жим гантелей", "Грудные мышцы"),
            Exercise("Приседания со штангой", "Ноги"),
            Exercise("Разгибания ног в тренажере", "Ноги"),
            Exercise("Сгибание ног в тренажере", "Ноги"),
            Exercise("Жим ногами", "Ноги"),
            Exercise("Выпады с гантелями", "Ноги"),
            Exercise("Присед", "Ноги"),
            Exercise("Румынская тяга", "Ноги"),
            Exercise("Болгарские выпады", "Ноги"),
            Exercise("Ягодичный мост", "Ноги"),
            Exercise("Сгибания на бицепс стоя", "Бицепсы"),
            Exercise("Сгибания рук со штангой", "Бицепсы"),
            Exercise("Разгибание на трицепс в кроссовере", "Трицепс"),
            Exercise("Разгибание гантели на трицепс", "Трицепс"),
            Exercise("Разведение рук встороны", "Плечи"),
            Exercise("Планка", "Пресс"),
            Exercise("Поднятие ног (нижний пресс)", "Пресс"),
            Exercise("Поднятие корпуса (верхний пресс)", "Пресс"),
            Exercise("Скручивание", "Пресс"),
        )

        // Фильтрация упражнений по группе мышц
        val filteredExercises = if (muscleGroupName != null) {
            allExercises.filter { it.muscleGroup == muscleGroupName }
        } else {
            allExercises // Если muscleGroupName == null, отображаем все упражнения
        }

        _exercises.value = filteredExercises
    }
}
