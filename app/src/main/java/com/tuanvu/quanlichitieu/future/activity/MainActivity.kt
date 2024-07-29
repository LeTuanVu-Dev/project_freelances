package com.tuanvu.quanlichitieu.future.activity

import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
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
import com.tuanvu.quanlichitieu.future.ultis.Constants

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
    private val incomeViewModel: IncomeViewModel by viewModels {
        IncomeViewModelFactory((application as MyApplication).incomeRepository)
    }
    private val expenseViewModel : ExpenseViewModel by viewModels {
        ExpenseViewModelFactory((application as MyApplication).expenseRepository)
    }

    private val incomeCategoriesViewModel : IncomeCategoriesViewModel by viewModels {
        IncomeCategoriesViewModelFactory((application as MyApplication).incomeCategoriesRepository)
    }
    private val expenseCategoriesViewModel: ExpenseCategoriesViewModel by viewModels {
        ExpenseCategoriesViewModelFactory((application as MyApplication).expenseCategoriesRepository)
    }
    override fun createView() {
        Log.d("VuLT", "createView: MainActivity")

        binding.tvNameUser.text = SharedPreferenceUtils.keyUserNameLogin
        binding.ctnSetting.setOnClickListener {
            startActivity(Intent(this,SettingActivity::class.java))
        }
        binding.ctnIncome.setOnClickListener {
            startActivity(Intent(this,IncomeActivity::class.java))
        }
        binding.ctnExpense.setOnClickListener {
            startActivity(Intent(this,ExpenseActivity::class.java))
        }

        binding.ctnIncomeCategory.setOnClickListener {
            startActivity(Intent(this,IncomeCategoriesActivity::class.java))
        }
        binding.ctnExpenseCategory.setOnClickListener {
            startActivity(Intent(this,ExpenseCategoriesActivity::class.java))
        }
//        insertDemoExpenseCategory()
//        insertDemoIncomeCategory()
    }

//    letuanvu.work@gmail.com
    private fun insertDemoIncome(){
        var item1 = Income(user_id = 2, category_id = 0, amount = 1f, description = "demo1", date = "11/12/2024", status = Constants.PAID)
        var item2 = Income(user_id = 2, category_id = 0, amount = 5f, description = "demo2", date = "11/9/2024", status = Constants.UNPAID)
        var item3 = Income(user_id = 2, category_id = 0, amount = 7f, description = "demo3", date = "11/11/2024", status = Constants.PAID)

        val list = listOf(
            item1,item2,item3
        )
          incomeViewModel.insertAll(list)
    }
    private fun insertDemoExpense(){
        var item1 = TableExpense(user_id = 2, category_id = 0, amount = 1f, description = "demo1", date = "11/12/2024", status = Constants.PAID)
        var item2 = TableExpense(user_id = 2, category_id = 0, amount = 5f, description = "demo2", date = "11/9/2024", status = Constants.UNPAID)
        var item3 = TableExpense(user_id = 2, category_id = 0, amount = 7f, description = "demo3", date = "11/11/2024", status = Constants.PAID)

        val list = listOf(
            item1,item2,item3
        )
        expenseViewModel.insertAll(list)
    }

    private fun insertDemoIncomeCategory(){
        var item1 = IncomeCategories(name = "catDemo1")
        var item2 = IncomeCategories(name = "catDemo2")
        var item3 = IncomeCategories(name = "catDemo3")

        val list = listOf(
            item1,item2,item3
        )
        incomeCategoriesViewModel.insertAll(list)
    }

    private fun insertDemoExpenseCategory(){
        var item1 = ExpenseCategories(name = "catDemo1")
        var item2 = ExpenseCategories(name = "catDemo2")
        var item3 = ExpenseCategories(name = "catDemo3")

        val list = listOf(
            item1,item2,item3
        )
        expenseCategoriesViewModel.insertAll(list)
    }

}