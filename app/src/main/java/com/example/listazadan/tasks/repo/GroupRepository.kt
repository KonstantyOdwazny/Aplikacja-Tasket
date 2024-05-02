package com.example.listazadan.tasks.repo

import com.example.listazadan.data.database.GroupDao
import com.example.listazadan.data.models.Group
import kotlinx.coroutines.flow.Flow

class GroupRepository(private val groupDao: GroupDao) {
    suspend fun insertGroup(group: Group){
        return groupDao.insertGroup(group)
    }

    fun getAllGroups(): Flow<List<Group>> {
        return groupDao.getAllGroups()
    }
}