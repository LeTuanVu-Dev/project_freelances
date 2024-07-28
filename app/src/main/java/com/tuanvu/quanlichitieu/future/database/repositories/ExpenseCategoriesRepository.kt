package com.tuanvu.quanlichitieu.future.database.repositories

import com.tuanvu.quanlichitieu.future.database.dao.ExpenseCategoriesDao
import com.tuanvu.quanlichitieu.future.database.entity.ExpenseCategories
import com.tuanvu.quanlichitieu.future.database.entity.IncomeCategories
import kotlinx.coroutines.flow.Flow

class ExpenseCategoriesRepository(private val expenseCategoriesDao: ExpenseCategoriesDao) {
    val allExpenseCategories : Flow<List<ExpenseCategories>> = expenseCategoriesDao.getAllExpenseCategories()


    suspend fun insert(templateHistory: ExpenseCategories) {
        expenseCategoriesDao.insert(templateHistory)
    }
    suspend fun insertGetId(templateHistory: ExpenseCategories): Long {
        return expenseCategoriesDao.insertGetId(templateHistory)
    }
    fun getExpenseCategoriesWithId(incomeId:Long): Flow<ExpenseCategories> {
        return expenseCategoriesDao.getExpenseCategoriesWithId(incomeId)
    }
    suspend fun insertAll(templateHistory: List<ExpenseCategories>) {
        expenseCategoriesDao.insertAll(templateHistory)
    }

    suspend fun update(templateHistory: ExpenseCategories) {
        expenseCategoriesDao.update(templateHistory)
    }

    suspend fun delete(templateHistory: ExpenseCategories) {
        expenseCategoriesDao.delete(templateHistory)
    }
}