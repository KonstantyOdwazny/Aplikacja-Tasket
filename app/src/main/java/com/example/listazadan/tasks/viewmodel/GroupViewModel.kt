package com.example.listazadan.tasks.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.listazadan.data.models.Group
import com.example.listazadan.tasks.repo.GroupRepository
import kotlinx.coroutines.launch

class GroupViewModel(private val repository: GroupRepository) : ViewModel() {

    fun addGroup(group: Group){
        viewModelScope.launch {
            repository.insertGroup(group)
        }
    }

    fun getGroupById(groupId: Int): LiveData<Group>{
        return repository.getGroupById(groupId)
    }

    fun updateGroup(group: Group){
        viewModelScope.launch {
            repository.updateGroup(group)
        }
    }

    fun deleteGroup(group: Group){
        viewModelScope.launch {
            repository.deleteGroup(group)
        }
    }

    val groups: LiveData<List<Group>> = repository.getAllGroups().asLiveData()

}