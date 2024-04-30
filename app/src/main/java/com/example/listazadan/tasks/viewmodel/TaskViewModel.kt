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
    fun getTaskById(taskId: Int): LiveData<Task> {
        return repository.getTaskById(taskId)
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
    // LiveData przechowująca wszystkie zadania
    val allTasks: LiveData<List<Task>> = repository.getAllTasks()
}


