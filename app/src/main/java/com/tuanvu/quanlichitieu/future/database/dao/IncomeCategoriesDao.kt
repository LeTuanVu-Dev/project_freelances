package com.tuanvu.quanlichitieu.future.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tuanvu.quanlichitieu.future.database.entity.IncomeCategories
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeCategoriesDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(incomeCategories: IncomeCategories)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGetId(incomeCategories : IncomeCategories): Long

    @Insert
    suspend fun insertAll(incomeCategories : List<IncomeCategories>)
    @Query("SELECT * FROM table_income_categories WHERE category_id = :categoryId")
    fun getIncomeCategoriesWithId(categoryId:Long): Flow<IncomeCategories>

    @Query("SELECT * FROM table_income_categories")
    fun getAllIncomeCategories(): Flow<List<IncomeCategories>>

    @Update
    suspend fun update(incomeCategories: IncomeCategories)

    @Delete
    suspend fun delete(incomeCategories: IncomeCategories)
}