package com.example.listazadan.data.models

enum class TaskFilter(val index: Int) {
    ALL(0),
    TODO(1),
    DONE(2);

    companion object {
        fun fromInt(value: Int) = entries.firstOrNull { it.index == value }
    }
}
