package com.tuanvu.quanlichitieu.future.database.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tuanvu.quanlichitieu.future.database.entity.IncomeCategories
import com.tuanvu.quanlichitieu.future.database.repositories.IncomeCategoriesRepository
import com.tuanvu.quanlichitieu.future.database.repositories.IncomeRepository
import kotlinx.coroutines.launch

class IncomeCategoriesViewModel(private val repository: IncomeCategoriesRepository) : ViewModel() {
    val allTableIncomeCategories: LiveData<List<IncomeCategories>> =
        repository.allIncomeCategories.asLiveData()

    fun getIncomeCategoriesWithId(audioId:Long): LiveData<IncomeCategories> {
        return repository.getIncomeCategoriesWithId(audioId).asLiveData()
    }

    fun insert(tableUser: IncomeCategories) {
        viewModelScope.launch {
            repository.insert(tableUser)
        }
    }

    fun insertGetId(tableUser: IncomeCategories, onInsertComplete: (Long) -> Unit) {
        viewModelScope.launch {
            val insertedId = repository.insertGetId(tableUser)
            onInsertComplete(insertedId)
        }
    }

    fun insertAll(tableUser: List<IncomeCategories>) {
        viewModelScope.launch {
            repository.insertAll(tableUser)
        }
    }

    fun update(tableUser: IncomeCategories) {
        viewModelScope.launch {
            repository.update(tableUser)
        }
    }

    fun delete(tableUser: IncomeCategories) {
        viewModelScope.launch {
            repository.delete(tableUser)
        }
    }
}

class IncomeCategoriesViewModelFactory(private val repository: IncomeCategoriesRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IncomeCategoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IncomeCategoriesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

