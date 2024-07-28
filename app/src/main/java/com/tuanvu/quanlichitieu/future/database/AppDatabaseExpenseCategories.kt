package com.tuanvu.quanlichitieu.future.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tuanvu.quanlichitieu.future.database.dao.ExpenseCategoriesDao
import com.tuanvu.quanlichitieu.future.database.dao.IncomeCategoriesDao
import com.tuanvu.quanlichitieu.future.database.dao.IncomeDao
import com.tuanvu.quanlichitieu.future.database.dao.TableUserDao
import com.tuanvu.quanlichitieu.future.database.entity.ExpenseCategories
import com.tuanvu.quanlichitieu.future.database.entity.Income
import com.tuanvu.quanlichitieu.future.database.entity.IncomeCategories
import com.tuanvu.quanlichitieu.future.database.entity.TableUser
import com.tuanvu.quanlichitieu.future.ultis.Constants

@Database(entities = [ExpenseCategories::class], version = 1, exportSchema = false)
abstract class AppDatabaseExpenseCategories : RoomDatabase() {
    abstract fun expenseCategoriesDao(): ExpenseCategoriesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabaseExpenseCategories? = null

        fun getDatabase(
            context: Context
        ): AppDatabaseExpenseCategories {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabaseExpenseCategories::class.java,
                    Constants.NAME_DATABASE_EXPENSE_CATEGORIES
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
