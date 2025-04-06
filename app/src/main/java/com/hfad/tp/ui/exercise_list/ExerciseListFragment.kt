package com.hfad.tp.ui.exercise_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hfad.tp.adapters.ExerciseAdapter
import com.hfad.tp.databinding.FragmentExerciseListBinding

// Класс фрагмента для отображения списка упражнений
class ExerciseListFragment : Fragment() {

    // Приватная переменная для привязки макета фрагмента
    private var _binding: FragmentExerciseListBinding? = null

    // Геттер для безопасного доступа к привязке макета
    private val binding get() = _binding!!

    // Переменная для хранения ViewModel фрагмента
    private lateinit var exerciseListViewModel: ExerciseListViewModel

    // Переменная для хранения адаптера RecyclerView
    private lateinit var exerciseAdapter: ExerciseAdapter

    // Метод создания представления фрагмента
    override fun onCreateView(
        inflater: LayoutInflater, // Инфлейтер для создания представления
        container: ViewGroup?, // Родительский контейнер для представления
        savedInstanceState: Bundle? // Сохраненные состояния фрагмента
    ): View {
        // Получение экземпляра ViewModel для управления данными фрагмента
        exerciseListViewModel = ViewModelProvider(this).get(ExerciseListViewModel::class.java)

        // Привязка макета фрагмента
        _binding = FragmentExerciseListBinding.inflate(inflater, container, false)

        // Получение корневого представления фрагмента
        val root: View = binding.root

        // Настройка RecyclerView для отображения списка упражнений
        binding.exerciseRecyclerView.layoutManager = LinearLayoutManager(context)

        // Инициализация адаптера для RecyclerView с пустым списком упражнений
        exerciseAdapter = ExerciseAdapter(mutableListOf())

        // Установка адаптера для RecyclerView
        binding.exerciseRecyclerView.adapter = exerciseAdapter

        // Получение имени группы мышц из аргументов фрагмента
        val muscleGroupName = arguments?.getString("muscleGroupName")

        // Наблюдение за данными упражнений в ViewModel
        exerciseListViewModel.exercises.observe(viewLifecycleOwner) { exercises ->
            // Фильтрация упражнений по группе мышц, если muscleGroupName задано
            val filteredExercises = if (muscleGroupName != null) {
                // Фильтрация упражнений, относящихся к заданной группе мышц
                exercises.filter { it.muscleGroup == muscleGroupName }
            } else {
                // Если muscleGroupName не задано, отображаем все упражнения
                exercises
            }

            // Обновление данных в адаптере с отфильтрованными упражнениями
            exerciseAdapter.updateData(filteredExercises)
        }

        // Загрузка упражнений из ViewModel с учетом фильтрации по группе мышц
        exerciseListViewModel.loadExercises(muscleGroupName)

        // Возвращение корневого представления фрагмента
        return root
    }

    // Метод вызываемый при уничтожении представления фрагмента
    override fun onDestroyView() {
        // Вызов родительского метода для уничтожения представления
        super.onDestroyView()

        // Обнуление привязки макета для предотвращения утечек памяти
        _binding = null
    }
}
