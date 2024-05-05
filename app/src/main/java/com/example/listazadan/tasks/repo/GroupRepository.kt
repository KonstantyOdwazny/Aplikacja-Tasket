package com.example.listazadan.tasks.repo

import androidx.lifecycle.LiveData
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

    suspend fun updateGroup(group: Group){
        return groupDao.updateGroup(group)
    }

    fun getGroupById(groupId: Int): LiveData<Group>{
        return groupDao.getGroupById(groupId)
    }

    suspend fun deleteGroup(group: Group){
        return groupDao.deleteGroup(group)
    }
}