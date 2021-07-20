package com.vytautas.dev.mytodo.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.vytautas.dev.mytodo.data.TodoDatabase
import com.vytautas.dev.mytodo.data.models.TodoData
import com.vytautas.dev.mytodo.data.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val todoDao = TodoDatabase.getDatabase(application).todoDao()
    private val repository: TodoRepository = TodoRepository(todoDao)

    val getAllData: LiveData<List<TodoData>> = repository.getAllData
    val sortByHighPriority: LiveData<List<TodoData>> = repository.sortByHighPriority
    val sortByLowPriority: LiveData<List<TodoData>> = repository.sortByLowPriority

    fun insertData(todoData: TodoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(todoData)
        }
    }

    fun updateData(todoData: TodoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(todoData)
        }
    }

    fun deleteItem(todoData: TodoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(todoData)
        }
    }

    fun deleteAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllData()
        }
    }

    fun searchDatabase(searchQuery: String): LiveData<List<TodoData>> {
        return repository.searchDatabase(searchQuery)
    }

}