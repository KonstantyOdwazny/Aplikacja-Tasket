package com.example.listazadan

import android.app.Application
import com.example.listazadan.database.AppDatabase
import com.example.listazadan.database.TaskRepository

class MyApp : Application() {
    val taskRepository by lazy {
        TaskRepository(AppDatabase.getDatabase(this).taskDao())
    }
}
