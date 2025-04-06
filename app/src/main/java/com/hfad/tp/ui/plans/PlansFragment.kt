package com.hfad.tp.ui.plans

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hfad.tp.adapters.WorkoutAdapter
import com.hfad.tp.data.ExerciseSet
import com.hfad.tp.data.Workout
import com.hfad.tp.databinding.FragmentPlansBinding
import com.hfad.tp.ui.journal.JournalViewModel
import java.util.Date

class PlansFragment : Fragment() {

    // Приватная переменная для привязки макета фрагмента
    private var _binding: FragmentPlansBinding? = null

    // Геттер для безопасного доступа к привязке макета
    private val binding get() = _binding!!

    // Переменная для хранения ViewModel фрагмента
    private lateinit var plansViewModel: PlansViewModel

    // Переменная для хранения адаптера RecyclerView
    private lateinit var workoutAdapter: WorkoutAdapter

    // Метод создания представления фрагмента
    override fun onCreateView(
        inflater: LayoutInflater, // Инфлейтер для создания представления
        container: ViewGroup?, // Родительский контейнер для представления
        savedInstanceState: Bundle? // Сохраненные состояния фрагмента
    ): View {
        // Получение экземпляра ViewModel для управления данными фрагмента
        plansViewModel = ViewModelProvider(this).get(PlansViewModel::class.java)

        // Привязка макета фрагмента
        _binding = FragmentPlansBinding.inflate(inflater, container, false)

        // Получение корневого представления фрагмента
        val root: View = binding.root

        // Настройка RecyclerView для отображения списка тренировок
        binding.workoutRecyclerView.layoutManager = LinearLayoutManager(context)

        // Инициализация адаптера для RecyclerView с пустым списком тренировок
        workoutAdapter = WorkoutAdapter(mutableListOf())

        // Установка адаптера для RecyclerView
        binding.workoutRecyclerView.adapter = workoutAdapter

        // Создание адаптера для Spinner групп мышц
        val muscleGroupAdapter = ArrayAdapter(
            requireContext(), // Контекст для адаптера
            R.layout.simple_spinner_dropdown_item, // Макет для элементов списка
            plansViewModel.muscleGroups // Список групп мышц
        )

        // Установка адаптера для Spinner групп мышц
        binding.muscleGroupSpinner.adapter = muscleGroupAdapter

        // Установка обработчика выбора группы мышц в Spinner
        binding.muscleGroupSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            // Метод вызываемый при выборе элемента в Spinner
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Получение выбранной группы мышц
                val selectedMuscleGroup = plansViewModel.muscleGroups[position]

                // Загрузка упражнений для выбранной группы мышц
                plansViewModel.loadExercisesForMuscleGroup(selectedMuscleGroup)
            }

            // Метод вызываемый, когда ничего не выбрано
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Обработка ситуации, когда ничего не выбрано
            }
        })

        // Наблюдение за упражнениями в ViewModel
        plansViewModel.exercises.observe(viewLifecycleOwner) { exercises ->
            // Формирование списка имен упражнений
            val exerciseNames = exercises.map { it.name }

            // Создание адаптера для Spinner упражнений
            val exerciseAdapter = ArrayAdapter(
                requireContext(), // Контекст для адаптера
                R.layout.simple_spinner_dropdown_item, // Макет для элементов списка
                exerciseNames // Список имен упражнений
            )

            // Установка адаптера для Spinner упражнений
            binding.exerciseSpinner.adapter = exerciseAdapter
        }

        // Установка обработчика кнопки "Добавить упражнение"
        binding.addExerciseButton.setOnClickListener {
            // Добавление упражнения в тренировку
            addExerciseToWorkout()
        }

        // Установка обработчика кнопки "Сохранить тренировку"
        binding.saveWorkoutButton.setOnClickListener {
            // Сохранение тренировки
            saveWorkout()
        }

        // Наблюдение за тренировками в ViewModel и обновление адаптера
        plansViewModel.workouts.observe(viewLifecycleOwner) { workouts ->
            workoutAdapter.updateData(workouts)
        }

        // Возвращение корневого представления фрагмента
        return root
    }

    // Метод добавления упражнения в тренировку
    private fun addExerciseToWorkout() {
        // Получение выбранного упражнения из Spinner
        val selectedExerciseName = binding.exerciseSpinner.selectedItem as? String

        // Поиск упражнения по имени
        val selectedExercise = plansViewModel.exercises.value?.find { it.name == selectedExerciseName }

        // Получение веса, повторений и подходов из полей ввода
        val weight = binding.weightEditText.text.toString().toDoubleOrNull() ?: 0.0
        val repetitions = binding.repetitionsEditText.text.toString().toIntOrNull() ?: 0
        val sets = binding.setsEditText.text.toString().toIntOrNull() ?: 0

        // Проверка наличия выбранного упражнения
        if (selectedExercise == null) {
            // Отображение сообщения об ошибке, если упражнение не выбрано
            Toast.makeText(context, "Выберите упражнение", Toast.LENGTH_SHORT).show()
            return
        }

        // Создание набора упражнений
        val exerciseSet = ExerciseSet(selectedExercise, weight, repetitions, sets)

        // Добавление набора упражнений в тренировку
        plansViewModel.addExerciseSet(exerciseSet)

        // Очистка полей ввода
        binding.weightEditText.text.clear()
        binding.repetitionsEditText.text.clear()
        binding.setsEditText.text.clear()
    }

    // Метод сохранения тренировки
    private fun saveWorkout() {
        // Получение названия тренировки из поля ввода
        val workoutName = binding.workoutNameEditText.text.toString()

        // Проверка наличия названия тренировки
        if (workoutName.isEmpty()) {
            // Отображение сообщения об ошибке, если название не введено
            Toast.makeText(context, "Введите название тренировки", Toast.LENGTH_SHORT).show()
            return
        }

        // Получение наборов упражнений из ViewModel
        val exerciseSets = plansViewModel.getExerciseSets()

        // Проверка наличия наборов упражнений
        if (exerciseSets.isEmpty()) {
            // Отображение сообщения об ошибке, если наборы упражнений не добавлены
            Toast.makeText(context, "Добавьте хотя бы одно упражнение", Toast.LENGTH_SHORT).show()
            return
        }

        // Создание тренировки с текущей датой и наборами упражнений
        val workout = Workout(Date(), workoutName, exerciseSets)

        // Сохранение тренировки в ViewModel
        plansViewModel.saveWorkout(workout)

        // Обновление данных в журнале после сохранения
        val journalViewModel = ViewModelProvider(this).get(JournalViewModel::class.java)
        journalViewModel.loadWorkoutsForDate(Date())

        // Отображение сообщения об успешном сохранении
        Toast.makeText(context, "Тренировка сохранена", Toast.LENGTH_SHORT).show()

        // Очистка поля ввода названия тренировки
        binding.workoutNameEditText.text.clear()
    }

    // Метод вызываемый при уничтожении представления фрагмента
    override fun onDestroyView() {
        // Вызов родительского метода для уничтожения представления
        super.onDestroyView()

        // Обнуление привязки макета для предотвращения утечек памяти
        _binding = null
    }
}
