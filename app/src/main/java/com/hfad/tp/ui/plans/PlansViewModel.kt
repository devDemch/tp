package com.hfad.tp.ui.plans

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hfad.tp.data.DatabaseHelper
import com.hfad.tp.data.Exercise
import com.hfad.tp.data.ExerciseSet
import com.hfad.tp.data.Workout
import com.hfad.tp.data.WorkoutDAO
import java.util.Date

class PlansViewModel(application: Application) : AndroidViewModel(application) {

    // Список групп мышц
    val muscleGroups = listOf(
        "Спина",
        "Грудные мышцы",
        "Ноги",
        "Плечи",
        "Бицепсы",
        "Трицепс",
        "Пресс"
    )

    // Полный список упражнений
    private val allExercises = listOf(
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

    // LiveData для отображения фильтрованного списка упражнений
    private val _exercises = MutableLiveData<List<Exercise>>().apply {
        // Инициализация со всеми упражнениями
        value = allExercises
    }

    // LiveData для доступа к списку упражнений
    val exercises: LiveData<List<Exercise>> = _exercises

    // LiveData для хранения наборов упражнений в текущей тренировке
    private val _workout = MutableLiveData<MutableList<ExerciseSet>>().apply {
        // Инициализация с пустым списком наборов упражнений
        value = mutableListOf()
    }

    // LiveData для доступа к текущей тренировке
    val workout: LiveData<MutableList<ExerciseSet>> = _workout

    // LiveData для хранения всех тренировок
    private val _workouts = MutableLiveData<List<Workout>>().apply {
        // Инициализация с пустым списком тренировок
        value = emptyList()
    }

    // LiveData для доступа к списку тренировок
    val workouts: LiveData<List<Workout>> = _workouts

    private val dbHelper = DatabaseHelper(application)
    private val workoutDAO = WorkoutDAO(dbHelper)

    // Метод добавления набора упражнений в текущую тренировку
    fun addExerciseSet(exerciseSet: ExerciseSet) {
        // Получение текущей тренировки
        val currentWorkout = _workout.value ?: mutableListOf()

        // Добавление набора упражнений в тренировку
        currentWorkout.add(exerciseSet)

        // Обновление текущей тренировки
        _workout.postValue(currentWorkout)
    }

    // Метод получения наборов упражнений текущей тренировки
    fun getExerciseSets(): List<ExerciseSet> {
        // Возвращение текущей тренировки или пустого списка, если она не задана
        return _workout.value ?: emptyList()
    }

    // Метод сохранения тренировки
    fun saveWorkout(workout: Workout) {
        try {
            // Получение текущего списка тренировок
            val currentWorkouts = _workouts.value ?: emptyList()

            // Добавление новой тренировки в список
            _workouts.postValue(currentWorkouts + workout)

            // Очистка текущей тренировки
            _workout.postValue(mutableListOf())

            // Сохранение тренировки в базе данных
            workoutDAO.addWorkout(workout)
        } catch (e: Exception) {
            // Логирование ошибок
            android.util.Log.e("PlansViewModel", "Ошибка при сохранении тренировки", e)
        }
    }

    // Метод загрузки упражнений для выбранной группы мышц
    fun loadExercisesForMuscleGroup(muscleGroup: String) {
        // Фильтрация упражнений по выбранной группе мышц
        _exercises.value = allExercises.filter { it.muscleGroup == muscleGroup }
    }
}
