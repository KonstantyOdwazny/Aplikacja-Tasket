package com.example.listazadan.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Update
import com.example.listazadan.data.models.Task  // Zaimportuj swój model Task

@Dao
interface TaskDao {
    // Deklaracja metody do wstawiania zadania do bazy danych
    @Insert
    suspend fun insertTask(task: Task)

    // Przykładowa metoda do pobierania wszystkich zadań
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskById(taskId: Int): LiveData<Task>

    @Query("SELECT * FROM tasks WHERE isCompleted = :completeMark")
    fun getTaskByCompleteCheck(completeMark: Boolean): LiveData<List<Task>>

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}

