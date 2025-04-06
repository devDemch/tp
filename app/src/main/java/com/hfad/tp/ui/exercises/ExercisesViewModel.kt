package com.hfad.tp.ui.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hfad.tp.R
import com.hfad.tp.data.Exercise
import com.hfad.tp.data.MuscleGroup

class ExercisesViewModel : ViewModel() {

    private val _muscleGroupsWithCounts = MutableLiveData<List<MuscleGroup>>().apply {
        value = getMuscleGroupsWithCounts()
    }
    val muscleGroupsWithCounts: LiveData<List<MuscleGroup>> = _muscleGroupsWithCounts

    private fun getMuscleGroupsWithCounts(): List<MuscleGroup> {
        return listOf(
            MuscleGroup("Спина", 8, R.drawable.back),
            MuscleGroup("Пресс", 4, R.drawable.press),
            MuscleGroup("Грудные мышцы", 4, R.drawable.pectoral_muscles),
            MuscleGroup("Бицепсы", 2, R.drawable.biceps),
            MuscleGroup("Трицепс", 2, R.drawable.triceps),
            MuscleGroup("Плечи", 1, R.drawable.shoulders),
            MuscleGroup("Ноги", 9, R.drawable.legs),
        )
    }
}
