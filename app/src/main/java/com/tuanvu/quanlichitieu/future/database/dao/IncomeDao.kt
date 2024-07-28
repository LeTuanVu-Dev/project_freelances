package com.tuanvu.quanlichitieu.future.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tuanvu.quanlichitieu.future.database.entity.Income
import com.tuanvu.quanlichitieu.future.database.entity.TableUser
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tableUser : Income)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGetId(tableUser : Income): Long

    @Insert
    suspend fun insertAll(template : List<Income>)
    @Query("SELECT * FROM table_income WHERE income_id = :audioId")
    fun getIncomeWithId(audioId:Long): Flow<Income>

    @Query("SELECT * FROM table_income WHERE user_id = :userId")
    fun getIncomeWithIdUser(userId:Long): Flow<List<Income>>

    @Query("SELECT * FROM table_income")
    fun getAllIncome(): Flow<List<Income>>

    @Update
    suspend fun update(tableUser: Income)

    @Delete
    suspend fun delete(tableUser: Income)
}