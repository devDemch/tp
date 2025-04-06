package com.hfad.tp.ui.journal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hfad.tp.adapters.WorkoutAdapter
import com.hfad.tp.data.Workout
import com.hfad.tp.databinding.FragmentJournalBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class JournalFragment : Fragment() {

    // Приватная переменная для привязки макета фрагмента
    private var _binding: FragmentJournalBinding? = null

    // Геттер для безопасного доступа к привязке макета
    private val binding get() = _binding!!

    // Переменная для хранения ViewModel фрагмента
    private lateinit var journalViewModel: JournalViewModel

    // Переменная для хранения адаптера RecyclerView
    private lateinit var workoutAdapter: WorkoutAdapter

    // Формат даты для отображения выбранной даты
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    // Переменная для хранения текущей выбранной даты
    private var selectedDate: Date = Date() // Текущая дата по умолчанию

    // Метод создания представления фрагмента
    override fun onCreateView(
        inflater: LayoutInflater, // Инфлейтер для создания представления
        container: ViewGroup?, // Родительский контейнер для представления
        savedInstanceState: Bundle? // Сохраненные состояния фрагмента
    ): View {
        // Получение экземпляра ViewModel для управления данными фрагмента
        journalViewModel = ViewModelProvider(this).get(JournalViewModel::class.java)

        // Привязка макета фрагмента
        _binding = FragmentJournalBinding.inflate(inflater, container, false)

        // Получение корневого представления фрагмента
        val root: View = binding.root

        // Настройка RecyclerView для отображения списка тренировок
        binding.workoutListRecyclerView.layoutManager = LinearLayoutManager(context)

        // Инициализация адаптера для RecyclerView с пустым списком тренировок
        workoutAdapter = WorkoutAdapter(mutableListOf())

        // Установка адаптера для RecyclerView
        binding.workoutListRecyclerView.adapter = workoutAdapter

        // Настройка CalendarView для выбора даты
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Создание календаря для формирования даты
            val calendar = Calendar.getInstance()

            // Установка выбранной даты в календаре
            calendar.set(year, month, dayOfMonth)

            // Обновление выбранной даты
            selectedDate = calendar.time

            // Обновление интерфейса с новой датой
            updateUI()
        }

        // Первичное обновление интерфейса с текущей датой
        updateUI()

        // Возвращение корневого представления фрагмента
        return root
    }

    // Метод обновления интерфейса фрагмента
    private fun updateUI() {
        // Обновление текста с выбранной датой
        binding.selectedDateTextView.text = dateFormat.format(selectedDate)

        // Загрузка тренировок для выбранной даты из ViewModel
        journalViewModel.loadWorkoutsForDate(selectedDate)

        // Наблюдение за тренировками в ViewModel и обновление адаптера
        journalViewModel.workouts.observe(viewLifecycleOwner) { workouts ->
            workoutAdapter.updateData(workouts)
        }

        // Наблюдение за статистикой в ViewModel и обновление текста статистики
        journalViewModel.statistics.observe(viewLifecycleOwner) { statistics ->
            binding.statisticsTextView.text = statistics
        }
    }

    // Метод вызываемый при уничтожении представления фрагмента
    override fun onDestroyView() {
        // Вызов родительского метода для уничтожения представления
        super.onDestroyView()

        // Обнуление привязки макета для предотвращения утечек памяти
        _binding = null
    }
}
