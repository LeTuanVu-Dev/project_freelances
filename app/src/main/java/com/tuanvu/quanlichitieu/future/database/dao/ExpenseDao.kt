package com.tuanvu.quanlichitieu.future.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tuanvu.quanlichitieu.future.database.entity.Income
import com.tuanvu.quanlichitieu.future.database.entity.TableExpense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tableExpense: TableExpense)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGetId(tableUser: TableExpense): Long

    @Insert
    suspend fun insertAll(tableExpense: List<TableExpense>)

    @Query("SELECT * FROM table_expense WHERE expense_id = :expenseID")
    fun getExpenseWithId(expenseID:Long): Flow<TableExpense>


    @Query("SELECT * FROM table_expense WHERE user_id = :userId")
    fun getTableExpenseWithIdUser(userId: Long): Flow<List<TableExpense>>

    @Query("SELECT * FROM table_expense")
    fun getAllTableExpense(): Flow<List<TableExpense>>

    @Update
    suspend fun update(tableExpense: TableExpense)

    @Delete
    suspend fun delete(tableExpense: TableExpense)
}