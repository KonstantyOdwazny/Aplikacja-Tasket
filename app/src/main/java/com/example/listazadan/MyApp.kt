package com.example.listazadan

import android.app.Application
import com.example.listazadan.data.database.AppDatabase
import com.example.listazadan.data.models.Group
import com.example.listazadan.tasks.repo.GroupRepository
import com.example.listazadan.tasks.repo.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initializeDefaultGroup()
    }

    val taskRepository by lazy {
        TaskRepository(AppDatabase.getDatabase(this).taskDao())
    }

    val groupRepository by lazy {
        GroupRepository(AppDatabase.getDatabase(this).groupDao())
    }

    private fun initializeDefaultGroup() {
        CoroutineScope(Dispatchers.IO).launch {
            val currentGroups = groupRepository.getAllGroups().first()
            if (currentGroups.none { it.name == "Defaults" }) {
                groupRepository.insertGroup(Group(name = "Defaults"))
            }
        }
    }

}
