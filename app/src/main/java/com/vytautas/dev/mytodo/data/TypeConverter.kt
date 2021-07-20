package com.vytautas.dev.mytodo.data

import androidx.room.TypeConverter
import com.vytautas.dev.mytodo.data.models.Priority

class TypeConverter {

    @TypeConverter
    fun toString(priority: Priority): String {
        return priority.name
    }

    @TypeConverter
    fun toPriority(data: String): Priority {
        return Priority.valueOf(data)
    }
}