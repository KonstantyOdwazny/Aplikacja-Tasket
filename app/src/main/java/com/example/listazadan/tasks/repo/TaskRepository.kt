package com.example.listazadan.tasks.repo

import androidx.lifecycle.LiveData
import com.example.listazadan.data.database.TaskDao
import com.example.listazadan.data.models.Task

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

