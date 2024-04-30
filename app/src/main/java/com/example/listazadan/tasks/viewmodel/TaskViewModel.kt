package com.example.listazadan.tasks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import com.example.listazadan.data.models.Task
import com.example.listazadan.tasks.repo.TaskRepository
import kotlinx.coroutines.launch


class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    // Funkcja do dodawania zadania używająca Kotlin Coroutines
    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }
    // LiveData przechowująca wszystkie zadania
    val allTasks: LiveData<List<Task>> = repository.getAllTasks()
}


