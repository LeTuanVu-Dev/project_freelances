package com.tuanvu.quanlichitieu.future.database.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tuanvu.quanlichitieu.future.database.entity.TableUser
import com.tuanvu.quanlichitieu.future.database.repositories.TableUserRepository
import kotlinx.coroutines.launch

class TableUserViewModel(private val repository: TableUserRepository) : ViewModel() {
    val allTableUser: LiveData<List<TableUser>> =
        repository.allTemplateHistories.asLiveData()


    fun insert(tableUser: TableUser) {
        viewModelScope.launch {
            repository.insert(tableUser)
        }
    }

    fun insertGetId(tableUser: TableUser, onInsertComplete: (Long) -> Unit) {
        viewModelScope.launch {
            val insertedId = repository.insertGetId(tableUser)
            onInsertComplete(insertedId)
        }
    }

    fun insertAll(tableUser: List<TableUser>) {
        viewModelScope.launch {
            repository.insertAll(tableUser)
        }
    }

    fun update(tableUser: TableUser) {
        viewModelScope.launch {
            repository.update(tableUser)
        }
    }

    fun delete(tableUser: TableUser) {
        viewModelScope.launch {
            repository.delete(tableUser)
        }
    }
}

class TableUserViewModelFactory(private val repository: TableUserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TableUserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TableUserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

