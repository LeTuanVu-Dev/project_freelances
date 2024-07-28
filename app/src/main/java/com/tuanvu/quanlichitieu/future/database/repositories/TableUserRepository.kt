package com.tuanvu.quanlichitieu.future.database.repositories

import com.tuanvu.quanlichitieu.future.database.dao.TableUserDao
import com.tuanvu.quanlichitieu.future.database.entity.TableUser
import kotlinx.coroutines.flow.Flow

class TableUserRepository(private val tableUserDao: TableUserDao) {
    val allTemplateHistories : Flow<List<TableUser>> = tableUserDao.getAllUser()


    suspend fun insert(templateHistory: TableUser) {
        tableUserDao.insert(templateHistory)
    }
    suspend fun insertGetId(templateHistory: TableUser): Long {
        return tableUserDao.insertGetId(templateHistory)
    }
    suspend fun insertAll(templateHistory: List<TableUser>) {
        tableUserDao.insertAll(templateHistory)
    }

    suspend fun update(templateHistory: TableUser) {
        tableUserDao.update(templateHistory)
    }

    suspend fun delete(templateHistory: TableUser) {
        tableUserDao.delete(templateHistory)
    }
}