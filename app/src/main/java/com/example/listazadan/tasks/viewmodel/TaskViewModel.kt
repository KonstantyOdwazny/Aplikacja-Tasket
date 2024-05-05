package com.example.listazadan.tasks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.listazadan.data.models.Task
import com.example.listazadan.data.models.TaskFilter
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

    var selectedTab: TaskFilter = TaskFilter.ALL
    val filteredTasks = MediatorLiveData<List<Task>>()
    var selectedGroup: Int = 1

    init {
        filteredTasks.addSource(allTasks) { tasks ->
            filteredTasks.value = filterTasks(tasks, selectedTab, selectedGroup)
        }
    }
    private fun filterTasks(tasks: List<Task>, tab: TaskFilter, groupID: Int): List<Task> {
        val filteredTasks = tasks.filter { it.groupId == groupID }
        return when (tab) {
            TaskFilter.TODO -> filteredTasks.filter { !it.isCompleted }
            TaskFilter.DONE -> filteredTasks.filter { it.isCompleted }
            else -> filteredTasks // All tasks
        }
    }
    fun onTabSelected(tabIndex: TaskFilter) {
        selectedTab = tabIndex
        filteredTasks.value = filterTasks(allTasks.value ?: emptyList(), selectedTab, selectedGroup)
    }

    fun onGroupSelected(groupID: Int){
        selectedGroup = groupID
        filteredTasks.value = filterTasks(allTasks.value ?: emptyList(), selectedTab, selectedGroup)
    }
}


