package com.example.listazadan.database

import androidx.lifecycle.LiveData

class TaskRepository(private val taskDao: TaskDao) {

    // Metoda asynchroniczna do wstawiania zadania
    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    // Metoda do pobierania wszystkich zadań
    fun getAllTasks(): LiveData<List<Task>> {
        return taskDao.getAllTasks()
    }
}

