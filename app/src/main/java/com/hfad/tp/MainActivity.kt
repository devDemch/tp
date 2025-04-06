package com.hfad.tp

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.hfad.tp.databinding.ActivityMainBinding

// Главный класс активности приложения, наследующий AppCompatActivity
class MainActivity : AppCompatActivity() {

    // Переменная для хранения привязки макета активности
    private lateinit var binding: ActivityMainBinding

    // Метод, вызываемый при создании активности
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация привязки макета активности через LayoutInflater
        binding = ActivityMainBinding.inflate(layoutInflater)

        // Установка корневого представления активности
        setContentView(binding.root)

        // Получение ссылки на BottomNavigationView из макета
        val navView: BottomNavigationView = binding.navView

        // Настройка контроллера навигации, связанного с фрагментом-хостом навигации
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Определение конфигурации верхнего уровня для AppBar (заголовка)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_exercises, // Идентификатор пункта меню "Упражнения"
                R.id.navigation_plans,    // Идентификатор пункта меню "Планы"
                R.id.navigation_journal   // Идентификатор пункта меню "Журнал"
            )
        )

        // Настройка ActionBar для работы с контроллером навигации и конфигурацией верхнего уровня
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Настройка BottomNavigationView для работы с контроллером навигации
        navView.setupWithNavController(navController)
    }
}
