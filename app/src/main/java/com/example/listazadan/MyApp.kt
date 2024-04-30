package com.example.listazadan

import android.app.Application
import com.example.listazadan.data.database.AppDatabase
import com.example.listazadan.tasks.repo.TaskRepository

class MyApp : Application() {
    val taskRepository by lazy {
        TaskRepository(AppDatabase.getDatabase(this).taskDao())
    }

}
