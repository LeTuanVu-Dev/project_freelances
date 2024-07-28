package com.tuanvu.quanlichitieu.future.database.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tuanvu.quanlichitieu.future.database.entity.ExpenseCategories
import com.tuanvu.quanlichitieu.future.database.entity.IncomeCategories
import com.tuanvu.quanlichitieu.future.database.repositories.ExpenseCategoriesRepository
import com.tuanvu.quanlichitieu.future.database.repositories.IncomeCategoriesRepository
import com.tuanvu.quanlichitieu.future.database.repositories.IncomeRepository
import kotlinx.coroutines.launch

class ExpenseCategoriesViewModel(private val repository: ExpenseCategoriesRepository) : ViewModel() {
    val allExpenseCategories: LiveData<List<ExpenseCategories>> =
        repository.allExpenseCategories.asLiveData()

    fun getIncomeCategoriesWithId(audioId:Long): LiveData<ExpenseCategories> {
        return repository.getExpenseCategoriesWithId(audioId).asLiveData()
    }

    fun insert(tableUser: ExpenseCategories) {
        viewModelScope.launch {
            repository.insert(tableUser)
        }
    }

    fun insertGetId(tableUser: ExpenseCategories, onInsertComplete: (Long) -> Unit) {
        viewModelScope.launch {
            val insertedId = repository.insertGetId(tableUser)
            onInsertComplete(insertedId)
        }
    }

    fun insertAll(tableUser: List<ExpenseCategories>) {
        viewModelScope.launch {
            repository.insertAll(tableUser)
        }
    }

    fun update(tableUser: ExpenseCategories) {
        viewModelScope.launch {
            repository.update(tableUser)
        }
    }

    fun delete(tableUser: ExpenseCategories) {
        viewModelScope.launch {
            repository.delete(tableUser)
        }
    }
}

class ExpenseCategoriesViewModelFactory(private val repository: ExpenseCategoriesRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseCategoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseCategoriesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

