package com.tuanvu.quanlichitieu.future.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tuanvu.quanlichitieu.future.database.entity.ExpenseCategories
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseCategoriesDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expenseCategories: ExpenseCategories)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGetId(expenseCategories: ExpenseCategories): Long

    @Insert
    suspend fun insertAll(expenseCategories : List<ExpenseCategories>)
    @Query("SELECT * FROM table_expense_categories WHERE category_id = :categoryId")
    fun getExpenseCategoriesWithId(categoryId:Long): Flow<ExpenseCategories>

    @Query("SELECT * FROM table_expense_categories")
    fun getAllExpenseCategories(): Flow<List<ExpenseCategories>>

    @Update
    suspend fun update(expenseCategories: ExpenseCategories)

    @Delete
    suspend fun delete(expenseCategories: ExpenseCategories)
}