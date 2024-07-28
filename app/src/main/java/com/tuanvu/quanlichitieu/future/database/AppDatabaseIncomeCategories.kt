package com.tuanvu.quanlichitieu.future.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tuanvu.quanlichitieu.future.database.dao.IncomeCategoriesDao
import com.tuanvu.quanlichitieu.future.database.dao.IncomeDao
import com.tuanvu.quanlichitieu.future.database.dao.TableUserDao
import com.tuanvu.quanlichitieu.future.database.entity.Income
import com.tuanvu.quanlichitieu.future.database.entity.IncomeCategories
import com.tuanvu.quanlichitieu.future.database.entity.TableUser
import com.tuanvu.quanlichitieu.future.ultis.Constants

@Database(entities = [IncomeCategories::class], version = 1, exportSchema = false)
abstract class AppDatabaseIncomeCategories : RoomDatabase() {
    abstract fun incomeCategoriesDao(): IncomeCategoriesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabaseIncomeCategories? = null

        fun getDatabase(
            context: Context
        ): AppDatabaseIncomeCategories {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabaseIncomeCategories::class.java,
                    Constants.NAME_DATABASE_INCOME_CATEGORIES
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
