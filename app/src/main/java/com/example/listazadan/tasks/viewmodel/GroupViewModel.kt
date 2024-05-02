package com.example.listazadan.tasks.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.listazadan.data.models.Group
import com.example.listazadan.tasks.repo.GroupRepository

class GroupViewModel(private val repository: GroupRepository) : ViewModel() {
    val groups: LiveData<List<Group>> = repository.getAllGroups().asLiveData()

}