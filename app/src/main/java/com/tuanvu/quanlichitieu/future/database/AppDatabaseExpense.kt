package com.tuanvu.quanlichitieu.future.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tuanvu.quanlichitieu.future.database.dao.ExpenseDao
import com.tuanvu.quanlichitieu.future.database.dao.IncomeDao
import com.tuanvu.quanlichitieu.future.database.dao.TableUserDao
import com.tuanvu.quanlichitieu.future.database.entity.Income
import com.tuanvu.quanlichitieu.future.database.entity.TableExpense
import com.tuanvu.quanlichitieu.future.database.entity.TableUser
import com.tuanvu.quanlichitieu.future.ultis.Constants

@Database(entities = [TableExpense::class], version = 1, exportSchema = false)
abstract class AppDatabaseExpense : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabaseExpense? = null

        fun getDatabase(
            context: Context
        ): AppDatabaseExpense {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabaseExpense::class.java,
                    Constants.NAME_DATABASE_EXPENSE
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
