package com.example.listazadan.tasks.repo

import androidx.lifecycle.LiveData
import com.example.listazadan.data.database.TaskDao
import com.example.listazadan.data.models.Task
import androidx.lifecycle.viewModelScope

class TaskRepository(private val taskDao: TaskDao) {

    // Metoda asynchroniczna do wstawiania zadania
    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    // Metoda do pobierania wszystkich zadań
    fun getAllTasks(): LiveData<List<Task>> {
        return taskDao.getAllTasks()
    }

    fun getTaskByCompleteCheck(completeMark: Boolean): LiveData<List<Task>> {
        return taskDao.getTaskByCompleteCheck(completeMark)
    }

    fun getTaskById(taskId: Int): LiveData<Task> {
        return taskDao.getTaskById(taskId)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task){
        taskDao.deleteTask(task)
    }
}

