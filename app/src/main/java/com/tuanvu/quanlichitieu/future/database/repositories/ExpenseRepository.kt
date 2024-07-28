package com.tuanvu.quanlichitieu.future.database.repositories

import com.tuanvu.quanlichitieu.future.database.dao.ExpenseDao
import com.tuanvu.quanlichitieu.future.database.entity.TableExpense
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val expenseDao: ExpenseDao) {
    val allTableExpense: Flow<List<TableExpense>> = expenseDao.getAllTableExpense()


    suspend fun insert(templateHistory: TableExpense) {
        expenseDao.insert(templateHistory)
    }

    suspend fun insertGetId(templateHistory: TableExpense): Long {
        return expenseDao.insertGetId(templateHistory)
    }
    fun getTableExpenseWithIdUser(userId: Long): Flow<List<TableExpense>> {
        return expenseDao.getTableExpenseWithIdUser(userId)
    }
    fun getTableExpenseWithId(expenseId: Long): Flow<TableExpense> {
        return expenseDao.getExpenseWithId(expenseId)
    }

    suspend fun insertAll(templateHistory: List<TableExpense>) {
        expenseDao.insertAll(templateHistory)
    }

    suspend fun update(templateHistory: TableExpense) {
        expenseDao.update(templateHistory)
    }

    suspend fun delete(templateHistory: TableExpense) {
        expenseDao.delete(templateHistory)
    }
}