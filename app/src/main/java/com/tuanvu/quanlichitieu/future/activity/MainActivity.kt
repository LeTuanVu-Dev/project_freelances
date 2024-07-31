package com.tuanvu.quanlichitieu.future.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tuanvu.quanlichitieu.base.BaseActivity
import com.tuanvu.quanlichitieu.databinding.ActivityMainBinding
import com.tuanvu.quanlichitieu.future.application.MyApplication
import com.tuanvu.quanlichitieu.future.database.entity.ExpenseCategories
import com.tuanvu.quanlichitieu.future.database.entity.Income
import com.tuanvu.quanlichitieu.future.database.entity.IncomeCategories
import com.tuanvu.quanlichitieu.future.database.entity.TableExpense
import com.tuanvu.quanlichitieu.future.database.viewmodel.ExpenseCategoriesViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.ExpenseCategoriesViewModelFactory
import com.tuanvu.quanlichitieu.future.database.viewmodel.ExpenseViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.ExpenseViewModelFactory
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeCategoriesViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeCategoriesViewModelFactory
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeViewModelFactory
import com.tuanvu.quanlichitieu.future.preferences.SharedPreferenceUtils
import com.tuanvu.quanlichitieu.future.service.MyService
import com.tuanvu.quanlichitieu.future.ultis.AppExtensions.setDailyAlarm
import com.tuanvu.quanlichitieu.future.ultis.Constants

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private val incomeViewModel: IncomeViewModel by viewModels {
        IncomeViewModelFactory((application as MyApplication).incomeRepository)
    }
    private val expenseViewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory((application as MyApplication).expenseRepository)
    }

    private val incomeCategoriesViewModel: IncomeCategoriesViewModel by viewModels {
        IncomeCategoriesViewModelFactory((application as MyApplication).incomeCategoriesRepository)
    }
    private val expenseCategoriesViewModel: ExpenseCategoriesViewModel by viewModels {
        ExpenseCategoriesViewModelFactory((application as MyApplication).expenseCategoriesRepository)
    }

    override fun createView() {
        Log.d("VuLT", "createView: MainActivity")

        binding.tvNameUser.text = SharedPreferenceUtils.keyUserNameLogin
        binding.ctnSetting.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }
        binding.ctnIncome.setOnClickListener {
            startActivity(Intent(this, IncomeActivity::class.java))
        }
        binding.ctnExpense.setOnClickListener {
            startActivity(Intent(this, ExpenseActivity::class.java))
        }

        binding.ctnIncomeCategory.setOnClickListener {
            startActivity(Intent(this, IncomeCategoriesActivity::class.java))
        }
        binding.ctnExpenseCategory.setOnClickListener {
            startActivity(Intent(this, ExpenseCategoriesActivity::class.java))
        }

        binding.ctnStatistics.setOnClickListener {
            startActivity(Intent(this, StaticsPainAndReceivedActivity::class.java))
        }
//        insertDemoExpenseCategory()
//        insertDemoIncomeCategory()
//        insertDemoIncome()
//        insertDemoExpense()
//        var item10 = Income(user_id = 1, category_id = 1, amount = 300f, description = "demo10", date = "1/1/2024", status = Constants.PAID)
//        incomeViewModel.insert(item10)
//        var item11 = TableExpense(user_id = 1, category_id = 1, amount = 13f, description = "demo10", date = "11/10/2024", status = Constants.RECEIVED)
//        expenseViewModel.insert(item11)
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SCHEDULE_EXACT_ALARM)
//            != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.SCHEDULE_EXACT_ALARM),
//                111
//            )
//        }
//        setDailyAlarm(this)
//        val serviceIntent = Intent(this, MyService::class.java)
//        startService(serviceIntent)
    }

    //    letuanvu.work@gmail.com
    private fun insertDemoIncome() {
        var item1 = Income(
            user_id = 1,
            category_id = 1,
            amount = 1f,
            description = "demo1",
            date = "1/1/2024",
            status = Constants.RECEIVED
        )
        var item2 = Income(
            user_id = 1,
            category_id = 1,
            amount = 2f,
            description = "demo2",
            date = "2/2/2024",
            status = Constants.NOT_RECEIVED
        )
        var item3 = Income(
            user_id = 1,
            category_id = 2,
            amount = 3f,
            description = "demo3",
            date = "3/3/2024",
            status = Constants.RECEIVED
        )
        var item4 = Income(
            user_id = 1,
            category_id = 1,
            amount = 4f,
            description = "demo4",
            date = "4/5/2024",
            status = Constants.RECEIVED
        )
        var item5 = Income(
            user_id = 1,
            category_id = 1,
            amount = 5f,
            description = "demo5",
            date = "5/4/2024",
            status = Constants.RECEIVED
        )
        var item6 = Income(
            user_id = 1,
            category_id = 1,
            amount = 16f,
            description = "demo6",
            date = "6/6/2024",
            status = Constants.RECEIVED
        )
        var item7 = Income(
            user_id = 1,
            category_id = 1,
            amount = 17f,
            description = "demo7",
            date = "7/7/2024",
            status = Constants.RECEIVED
        )
        var item8 = Income(
            user_id = 1,
            category_id = 1,
            amount = 8f,
            description = "demo8",
            date = "8/8/2024",
            status = Constants.RECEIVED
        )
        var item9 = Income(
            user_id = 1,
            category_id = 1,
            amount = 9f,
            description = "demo9",
            date = "9/9/2024",
            status = Constants.RECEIVED
        )
//        var item10 = Income(user_id = 1, category_id = 1, amount = 13f, description = "demo10", date = "11/10/2024", status = Constants.PAID)

        val list = listOf(
            item1, item2, item3, item4, item5, item6, item7, item8, item9//,item10
        )
        incomeViewModel.insertAll(list)
    }

    private fun insertDemoExpense() {
        var item1 = TableExpense(
            user_id = 1,
            category_id = 1,
            amount = 10f,
            description = "demo1",
            date = "1/1/2024",
            status = Constants.PAID
        )
        var item2 = TableExpense(
            user_id = 1,
            category_id = 1,
            amount = 7f,
            description = "demo2",
            date = "2/2/2024",
            status = Constants.UNPAID
        )
        var item3 = TableExpense(
            user_id = 1,
            category_id = 2,
            amount = 6f,
            description = "demo3",
            date = "3/3/2024",
            status = Constants.PAID
        )
        var item4 = TableExpense(
            user_id = 1,
            category_id = 1,
            amount = 2f,
            description = "demo4",
            date = "4/5/2024",
            status = Constants.PAID
        )
        var item5 = TableExpense(
            user_id = 1,
            category_id = 1,
            amount = 8f,
            description = "demo5",
            date = "5/4/2024",
            status = Constants.PAID
        )
        var item6 = TableExpense(
            user_id = 1,
            category_id = 1,
            amount = 9f,
            description = "demo6",
            date = "6/6/2024",
            status = Constants.PAID
        )
        var item7 = TableExpense(
            user_id = 1,
            category_id = 1,
            amount = 1f,
            description = "demo7",
            date = "7/7/2024",
            status = Constants.PAID
        )
        var item8 = TableExpense(
            user_id = 1,
            category_id = 1,
            amount = 11f,
            description = "demo8",
            date = "8/8/2024",
            status = Constants.PAID
        )
        var item9 = TableExpense(
            user_id = 1,
            category_id = 1,
            amount = 9f,
            description = "demo9",
            date = "9/9/2024",
            status = Constants.PAID
        )
//        var item10 = TableExpense(user_id = 1, category_id = 1, amount = 13f, description = "demo10", date = "11/10/2024", status = Constants.P)

        val list = listOf(
            item1, item2, item3, item4, item5, item6, item7, item8, item9//,item10
        )
        expenseViewModel.insertAll(list)
    }

    private fun insertDemoIncomeCategory() {
        var item1 = IncomeCategories(name = "catDemo1")
        var item2 = IncomeCategories(name = "catDemo2")
        var item3 = IncomeCategories(name = "catDemo3")
        var item4 = IncomeCategories(name = "catDemo4")
        var item5 = IncomeCategories(name = "catDemo5")
        var item6 = IncomeCategories(name = "catDemo6")
        var item7 = IncomeCategories(name = "catDemo7")
        var item8 = IncomeCategories(name = "catDemo8")
        var item9 = IncomeCategories(name = "catDemo9")
        var item10 = IncomeCategories(name = "catDemo10")

        val list = listOf(
            item1, item2, item3, item4, item5, item6, item7, item8, item9, item10
        )
        incomeCategoriesViewModel.insertAll(list)
    }

    private fun insertDemoExpenseCategory() {
        var item1 = ExpenseCategories(name = "catDemo1")
        var item2 = ExpenseCategories(name = "catDemo2")
        var item3 = ExpenseCategories(name = "catDemo3")
        var item4 = ExpenseCategories(name = "catDemo4")
        var item5 = ExpenseCategories(name = "catDemo5")
        var item6 = ExpenseCategories(name = "catDemo6")
        var item7 = ExpenseCategories(name = "catDemo7")
        var item8 = ExpenseCategories(name = "catDemo8")
        var item9 = ExpenseCategories(name = "catDemo9")
        var item10 = ExpenseCategories(name = "catDemo10")

        val list = listOf(
            item1, item2, item3, item4, item5, item6, item7, item8, item9, item10
        )
        expenseCategoriesViewModel.insertAll(list)
    }

}