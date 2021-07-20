package com.vytautas.dev.mytodo.data.repository

import androidx.lifecycle.LiveData
import com.vytautas.dev.mytodo.data.TodoDao
import com.vytautas.dev.mytodo.data.models.TodoData

class TodoRepository(private val todoDao: TodoDao) {

    val getAllData: LiveData<List<TodoData>> = todoDao.getAllData()
    val sortByHighPriority: LiveData<List<TodoData>> = todoDao.sortByHighPriority()
    val sortByLowPriority: LiveData<List<TodoData>> = todoDao.sortByLowPriority()

    suspend fun insertData(todoData: TodoData) {
        todoDao.insertData(todoData)
    }

    suspend fun updateData(todoData: TodoData) {
        todoDao.updateData(todoData)
    }

    suspend fun deleteItem(todoData: TodoData) {
        todoDao.deleteItem(todoData)
    }

    suspend fun deleteAllData() {
        todoDao.deleteAllData()
    }

    fun searchDatabase(searchQuery: String): LiveData<List<TodoData>> {
        return todoDao.searchDatabase(searchQuery)
    }
}