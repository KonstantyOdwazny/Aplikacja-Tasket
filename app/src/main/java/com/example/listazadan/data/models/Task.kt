package com.example.listazadan.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(tableName = "tasks",
    foreignKeys = [
        ForeignKey(entity = Group::class,
            parentColumns = ["groupId"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE)
    ])
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "isCompleted") val isCompleted: Boolean,
    val groupId: Int // klucz obcy wskazujący na grupę
)

