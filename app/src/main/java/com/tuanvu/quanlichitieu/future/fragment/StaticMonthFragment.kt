package com.tuanvu.quanlichitieu.future.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tuanvu.quanlichitieu.base.BaseFragment
import com.tuanvu.quanlichitieu.databinding.FragmentStaticsDayBinding
import com.tuanvu.quanlichitieu.future.application.MyApplication
import com.tuanvu.quanlichitieu.future.database.entity.Income
import com.tuanvu.quanlichitieu.future.database.entity.TableExpense
import com.tuanvu.quanlichitieu.future.database.viewmodel.ExpenseViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.ExpenseViewModelFactory
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeViewModelFactory
import com.tuanvu.quanlichitieu.future.epoxy.controller.SumController
import com.tuanvu.quanlichitieu.future.ultis.BarChartView
import com.tuanvu.quanlichitieu.future.ultis.Constants
import com.tuanvu.quanlichitieu.future.ultis.DateAmount
import java.text.SimpleDateFormat
import java.util.Locale

class StaticMonthFragment : BaseFragment<FragmentStaticsDayBinding>() {
    companion object {
        fun instance(): StaticMonthFragment {
            return newInstance(StaticMonthFragment::class.java)
        }
    }
    private val incomeViewModel: IncomeViewModel by viewModels {
        IncomeViewModelFactory((requireActivity().application as MyApplication).incomeRepository)
    }

    private val expenseViewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory((requireActivity().application as MyApplication).expenseRepository)
    }
    private var sumController = SumController()

    private var listItemPaid = arrayListOf<Income>()
    private var listItemReceived = arrayListOf<TableExpense>()
    // Lớp DateAmount để chứa dữ liệu đã lọc theo tháng và năm

    val matchingItems = arrayListOf<DateAmount>()

    val dateFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())  // Định dạng cho ngày dạng: 9/7/2024
    val monthYearFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())
    override fun initView() {
        incomeViewModel.allTableIncome.observe(this) { lstIncome ->
            if (lstIncome.isNotEmpty() && listItemPaid.isEmpty()) {
                // Lọc danh sách income với state = true
                val filteredIncomeList = lstIncome.filter { it.status == Constants.PAID }
                listItemPaid.addAll(filteredIncomeList)

                expenseViewModel.allTableExpense.observe(this) { lstExpense ->
                    if (lstExpense.isNotEmpty() && listItemReceived.isEmpty()) {
                        // Lọc danh sách expense với state = true
                        val filteredExpenseList = lstExpense.filter { it.status == Constants.RECEIVED }
                        listItemReceived.addAll(filteredExpenseList)

                        // Lấy danh sách tháng/năm từ danh sách income và expense
                        val incomeMonthYears = listItemPaid.map {
                            monthYearFormat.format(dateFormat.parse(it.date)!!)
                        }.toSet()
                        val expenseMonthYears = listItemReceived.map {
                            monthYearFormat.format(dateFormat.parse(it.date)!!)
                        }.toSet()
                        val allMonthYears = incomeMonthYears union expenseMonthYears

                        allMonthYears.forEach { monthYear ->
                            val incomeAmount = listItemPaid
                                .filter { monthYearFormat.format(dateFormat.parse(it.date)!!) == monthYear }
                                .sumByDouble { it.amount.toDouble() }
                            val expenseAmount = listItemReceived
                                .filter { monthYearFormat.format(dateFormat.parse(it.date)!!) == monthYear }
                                .sumByDouble { it.amount.toDouble() }
                            matchingItems.add(
                                DateAmount(
                                    monthYear.split("/").first(),  // Lưu trữ tháng/năm thay vì chỉ tháng
                                    incomeAmount.toFloat(),
                                    expenseAmount.toFloat()
                                )
                            )
                        }
                        sumController.setDataListItem(matchingItems)
                        binding.epxList.setControllerAndBuildModels(sumController)
                        // Ví dụ: In ra các giá trị trong danh sách matchingItems
                        binding.pieChart.submitData(mutableListOf<BarChartView.DataChart>().apply {
                            matchingItems.forEach { item ->
                                add(
                                    BarChartView.DataChart(
                                        item.incomeAmount,
                                        item.expenseAmount,
                                        item.date
                                    )
                                )
                            }
                        })
                    }
                }
            }
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStaticsDayBinding {
        return FragmentStaticsDayBinding.inflate(inflater,container,false)
    }
}