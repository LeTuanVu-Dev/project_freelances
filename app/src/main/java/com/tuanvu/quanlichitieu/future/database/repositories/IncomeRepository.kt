package com.tuanvu.quanlichitieu.future.database.repositories

import com.tuanvu.quanlichitieu.future.database.dao.IncomeDao
import com.tuanvu.quanlichitieu.future.database.dao.TableUserDao
import com.tuanvu.quanlichitieu.future.database.entity.Income
import com.tuanvu.quanlichitieu.future.database.entity.TableUser
import kotlinx.coroutines.flow.Flow

class IncomeRepository(private val incomeDao: IncomeDao) {
    val allIncome : Flow<List<Income>> = incomeDao.getAllIncome()


    suspend fun insert(templateHistory: Income) {
        incomeDao.insert(templateHistory)
    }
    suspend fun insertGetId(templateHistory: Income): Long {
        return incomeDao.insertGetId(templateHistory)
    }
    fun getIncomeWithId(incomeId:Long): Flow<Income> {
        return incomeDao.getIncomeWithId(incomeId)
    }
    fun getIncomeWithIdUser(userId:Long): Flow<List<Income>> {
        return incomeDao.getIncomeWithIdUser(userId)
    }
    suspend fun insertAll(templateHistory: List<Income>) {
        incomeDao.insertAll(templateHistory)
    }

    suspend fun update(templateHistory: Income) {
        incomeDao.update(templateHistory)
    }

    suspend fun delete(templateHistory: Income) {
        incomeDao.delete(templateHistory)
    }
}