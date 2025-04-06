// Определение пакета для фрагмента упражнений
package com.hfad.tp.ui.exercises

// Импорт необходимых классов для работы с фрагментами и RecyclerView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hfad.tp.R
import com.hfad.tp.adapters.MuscleGroupAdapter
import com.hfad.tp.databinding.FragmentExercisesBinding

// Класс фрагмента для отображения списка групп мышц
class ExercisesFragment : Fragment() {

    // Приватная переменная для привязки макета фрагмента
    private var _binding: FragmentExercisesBinding? = null

    // Геттер для безопасного доступа к привязке макета
    private val binding get() = _binding!!

    // Переменная для хранения ViewModel фрагмента
    private lateinit var exercisesViewModel: ExercisesViewModel

    // Переменная для хранения адаптера RecyclerView
    private lateinit var muscleGroupAdapter: MuscleGroupAdapter

    // Метод создания представления фрагмента
    override fun onCreateView(
        inflater: LayoutInflater, // Инфлейтер для создания представления
        container: ViewGroup?, // Родительский контейнер для представления
        savedInstanceState: Bundle? // Сохраненные состояния фрагмента
    ): View {
        // Получение экземпляра ViewModel для управления данными фрагмента
        exercisesViewModel = ViewModelProvider(this).get(ExercisesViewModel::class.java)

        // Привязка макета фрагмента
        _binding = FragmentExercisesBinding.inflate(inflater, container, false)

        // Получение корневого представления фрагмента
        val root: View = binding.root

        // Настройка RecyclerView для отображения списка групп мышц
        binding.muscleGroupsRecyclerView.layoutManager = LinearLayoutManager(context)

        // Инициализация адаптера для RecyclerView с обработкой клика на группу мышц
        muscleGroupAdapter = MuscleGroupAdapter(mutableListOf()) { muscleGroup ->
            // Обработка клика на группу мышц: переход к списку упражнений
            val bundle = Bundle().apply {
                // Добавление имени группы мышц в аргументы перехода
                putString("muscleGroupName", muscleGroup.name)
            }
            // Навигация к фрагменту списка упражнений с передачей аргументов
            findNavController().navigate(R.id.action_exercises_to_exerciseList, bundle)
        }

        // Установка адаптера для RecyclerView
        binding.muscleGroupsRecyclerView.adapter = muscleGroupAdapter

        // Наблюдение за данными групп мышц в ViewModel
        exercisesViewModel.muscleGroupsWithCounts.observe(viewLifecycleOwner) { muscleGroups ->
            // Обновление данных в адаптере с полученными группами мышц
            muscleGroupAdapter.updateData(muscleGroups)
        }

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
