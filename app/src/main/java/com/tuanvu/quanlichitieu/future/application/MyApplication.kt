package com.tuanvu.quanlichitieu.future.application

import android.app.Application
import android.util.Log
import com.its.baseapp.its.preferences.SharedPreferencesManager
import com.tuanvu.quanlichitieu.future.database.AppDatabase
import com.tuanvu.quanlichitieu.future.database.AppDatabaseExpense
import com.tuanvu.quanlichitieu.future.database.AppDatabaseExpenseCategories
import com.tuanvu.quanlichitieu.future.database.AppDatabaseIncome
import com.tuanvu.quanlichitieu.future.database.AppDatabaseIncomeCategories
import com.tuanvu.quanlichitieu.future.database.repositories.ExpenseCategoriesRepository
import com.tuanvu.quanlichitieu.future.database.repositories.ExpenseRepository
import com.tuanvu.quanlichitieu.future.database.repositories.IncomeCategoriesRepository
import com.tuanvu.quanlichitieu.future.database.repositories.IncomeRepository
import com.tuanvu.quanlichitieu.future.database.repositories.TableUserRepository

class MyApplication : Application() {

    companion object {
        lateinit var instanceApp: MyApplication
        lateinit var instanceSharePreference: SharedPreferencesManager
    }
    private val databaseUser by lazy { AppDatabase.getDatabase(this) }
    private val databaseIncome by lazy { AppDatabaseIncome.getDatabase(this) }
    private val databaseIncomeCategories by lazy { AppDatabaseIncomeCategories.getDatabase(this) }
    private val databaseExpense by lazy { AppDatabaseExpense.getDatabase(this) }
    private val databaseExpenseCategories by lazy { AppDatabaseExpenseCategories.getDatabase(this) }

    val tableUserRepository by lazy { TableUserRepository(databaseUser.tableUserDao()) }
    val incomeRepository by lazy { IncomeRepository(databaseIncome.incomeDao()) }
    val expenseRepository by lazy { ExpenseRepository(databaseExpense.expenseDao()) }
    val expenseCategoriesRepository by lazy { ExpenseCategoriesRepository(databaseExpenseCategories.expenseCategoriesDao()) }
    val incomeCategoriesRepository by lazy { IncomeCategoriesRepository(databaseIncomeCategories.incomeCategoriesDao()) }

    override fun onCreate() {
        super.onCreate()
        Log.d("Cuong", "onCreate: ")
        instanceApp = this
        instanceSharePreference = SharedPreferencesManager(this)

    }
}