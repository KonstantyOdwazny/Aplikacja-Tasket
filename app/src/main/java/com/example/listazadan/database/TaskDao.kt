package com.example.listazadan.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Entity
import androidx.room.PrimaryKey

import androidx.lifecycle.LiveData
import com.example.listazadan.database.Task  // Zaimportuj swój model Task

@Dao
interface TaskDao {
    // Deklaracja metody do wstawiania zadania do bazy danych
    @Insert
    suspend fun insertTask(task: Task)

    // Przykładowa metoda do pobierania wszystkich zadań
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): LiveData<List<Task>>
}

