package com.tuanvu.quanlichitieu.future.database.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tuanvu.quanlichitieu.future.database.entity.Income
import com.tuanvu.quanlichitieu.future.database.entity.TableExpense
import com.tuanvu.quanlichitieu.future.database.repositories.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {
    val allTableExpense: LiveData<List<TableExpense>> =
        repository.allTableExpense.asLiveData()

    fun getExpenseWithId(idExpense: Long): LiveData<TableExpense> {
        return repository.getTableExpenseWithId(idExpense).asLiveData()
    }
    fun getExpenseWithUserId(idUser: Long): LiveData<List<TableExpense>> {
        return repository.getTableExpenseWithIdUser(idUser).asLiveData()
    }
    fun getExpenseWithUserIdService(idUser:Long): Flow<List<TableExpense>> {
        return repository.getTableExpenseWithIdUser(idUser)
    }
    fun insert(tableUser: TableExpense) {
        viewModelScope.launch {
            repository.insert(tableUser)
        }
    }

    fun insertGetId(tableUser: TableExpense, onInsertComplete: (Long) -> Unit) {
        viewModelScope.launch {
            val insertedId = repository.insertGetId(tableUser)
            onInsertComplete(insertedId)
        }
    }

    fun insertAll(tableUser: List<TableExpense>) {
        viewModelScope.launch {
            repository.insertAll(tableUser)
        }
    }

    fun update(tableUser: TableExpense) {
        viewModelScope.launch {
            repository.update(tableUser)
        }
    }

    fun delete(tableUser: TableExpense) {
        viewModelScope.launch {
            repository.delete(tableUser)
        }
    }
}

class ExpenseViewModelFactory(private val repository: ExpenseRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

