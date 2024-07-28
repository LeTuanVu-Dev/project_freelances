package com.tuanvu.quanlichitieu.future.database.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tuanvu.quanlichitieu.future.database.entity.Income
import com.tuanvu.quanlichitieu.future.database.entity.TableUser
import com.tuanvu.quanlichitieu.future.database.repositories.IncomeRepository
import com.tuanvu.quanlichitieu.future.database.repositories.TableUserRepository
import kotlinx.coroutines.launch

class IncomeViewModel(private val repository: IncomeRepository) : ViewModel() {
    val allTableIncome: LiveData<List<Income>> =
        repository.allIncome.asLiveData()

    fun getIncomeWithId(audioId:Long): LiveData<Income> {
        return repository.getIncomeWithId(audioId).asLiveData()
    }

    fun getIncomeWithUserId(idUser:Long): LiveData<List<Income>> {
        return repository.getIncomeWithIdUser(idUser).asLiveData()
    }
    fun insert(tableUser: Income) {
        viewModelScope.launch {
            repository.insert(tableUser)
        }
    }

    fun insertGetId(tableUser: Income, onInsertComplete: (Long) -> Unit) {
        viewModelScope.launch {
            val insertedId = repository.insertGetId(tableUser)
            onInsertComplete(insertedId)
        }
    }

    fun insertAll(tableUser: List<Income>) {
        viewModelScope.launch {
            repository.insertAll(tableUser)
        }
    }

    fun update(tableUser: Income) {
        viewModelScope.launch {
            repository.update(tableUser)
        }
    }

    fun delete(tableUser: Income) {
        viewModelScope.launch {
            repository.delete(tableUser)
        }
    }
}

class IncomeViewModelFactory(private val repository: IncomeRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IncomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IncomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

