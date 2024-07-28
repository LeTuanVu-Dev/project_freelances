package com.tuanvu.quanlichitieu.future.database.repositories

import com.tuanvu.quanlichitieu.future.database.dao.IncomeCategoriesDao
import com.tuanvu.quanlichitieu.future.database.entity.IncomeCategories
import kotlinx.coroutines.flow.Flow

class IncomeCategoriesRepository(private val incomeCategoriesDao: IncomeCategoriesDao) {
    val allIncomeCategories : Flow<List<IncomeCategories>> = incomeCategoriesDao.getAllIncomeCategories()


    suspend fun insert(templateHistory: IncomeCategories) {
        incomeCategoriesDao.insert(templateHistory)
    }
    suspend fun insertGetId(templateHistory: IncomeCategories): Long {
        return incomeCategoriesDao.insertGetId(templateHistory)
    }
    fun getIncomeCategoriesWithId(incomeId:Long): Flow<IncomeCategories> {
        return incomeCategoriesDao.getIncomeCategoriesWithId(incomeId)
    }
    suspend fun insertAll(templateHistory: List<IncomeCategories>) {
        incomeCategoriesDao.insertAll(templateHistory)
    }

    suspend fun update(templateHistory: IncomeCategories) {
        incomeCategoriesDao.update(templateHistory)
    }

    suspend fun delete(templateHistory: IncomeCategories) {
        incomeCategoriesDao.delete(templateHistory)
    }
}