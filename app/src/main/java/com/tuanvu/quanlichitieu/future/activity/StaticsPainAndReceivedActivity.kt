package com.tuanvu.quanlichitieu.future.activity

import android.content.Intent
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.tuanvu.quanlichitieu.R
import com.tuanvu.quanlichitieu.base.BaseActivity
import com.tuanvu.quanlichitieu.databinding.ActivityStatiticsAllBinding
import com.tuanvu.quanlichitieu.databinding.ActivityStatiticsBinding
import com.tuanvu.quanlichitieu.future.application.MyApplication
import com.tuanvu.quanlichitieu.future.database.entity.Income
import com.tuanvu.quanlichitieu.future.database.entity.TableExpense
import com.tuanvu.quanlichitieu.future.database.viewmodel.ExpenseViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.ExpenseViewModelFactory
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeViewModelFactory
import com.tuanvu.quanlichitieu.future.ultis.BarChartView
import com.tuanvu.quanlichitieu.future.ultis.Constants
import kotlin.random.Random


class StaticsPainAndReceivedActivity : BaseActivity<ActivityStatiticsAllBinding>() {
    override fun getViewBinding(): ActivityStatiticsAllBinding {
        return ActivityStatiticsAllBinding.inflate(layoutInflater)
    }
    private val DAY_FRAGMENT = 0
    private val MONTH_FRAGMENT = 1
    private val YEAR_FRAGMENT = 2
    private var currentFragment = DAY_FRAGMENT
    override fun createView() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.ctnExpense.setOnClickListener {
            startActivity(Intent(this,StaticsActivity::class.java))
        }

        binding.ctnNotExpense.setOnClickListener {
            startActivity(Intent(this,StaticsUnActivity::class.java))
        }

    }

}