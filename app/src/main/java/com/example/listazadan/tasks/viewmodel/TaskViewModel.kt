package com.example.listazadan.tasks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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

    var selectedTab: Int = 0
    val filteredTasks = MediatorLiveData<List<Task>>()

    init {
        filteredTasks.addSource(allTasks) { tasks ->
            filteredTasks.value = filterTasks(tasks, selectedTab)
        }
    }
    private fun filterTasks(tasks: List<Task>, tab: Int): List<Task> {
        return when (tab) {
            1 -> tasks.filter { !it.isCompleted }
            2 -> tasks.filter { it.isCompleted }
            else -> tasks // All tasks
        }
    }
    fun onTabSelected(tabIndex: Int) {
        selectedTab = tabIndex
        filteredTasks.value = filterTasks(allTasks.value ?: emptyList(), selectedTab)
    }
}


