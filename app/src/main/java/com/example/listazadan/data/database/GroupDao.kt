package com.example.listazadan.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.listazadan.data.models.Group
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Insert
    suspend fun insertGroup(group: Group)

    @Query("SELECT * FROM groups")
    fun getAllGroups(): Flow<List<Group>>
}