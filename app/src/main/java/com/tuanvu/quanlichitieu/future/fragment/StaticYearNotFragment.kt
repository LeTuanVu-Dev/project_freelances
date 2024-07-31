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

class StaticYearNotFragment : BaseFragment<FragmentStaticsDayBinding>() {
    companion object {
        fun instance(): StaticYearNotFragment {
            return newInstance(StaticYearNotFragment::class.java)
        }
    }
    private val incomeViewModel: IncomeViewModel by viewModels {
        IncomeViewModelFactory((requireActivity().application as MyApplication).incomeRepository)
    }

    private val expenseViewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory((requireActivity().application as MyApplication).expenseRepository)
    }

    private var listItemPaid = arrayListOf<Income>()
    private var listItemReceived = arrayListOf<TableExpense>()


    val matchingItems = arrayListOf<DateAmount>()
    private var sumController = SumController()

    val dateFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())  // Định dạng cho ngày dạng: 9/7/2024
    val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    override fun initView() {
        incomeViewModel.allTableIncome.observe(this) { lstIncome ->
            if (lstIncome.isNotEmpty() && listItemPaid.isEmpty()) {
                // Lọc danh sách income với state = true
                val filteredIncomeList = lstIncome.filter { it.status == Constants.UNPAID }
                listItemPaid.addAll(filteredIncomeList)

                expenseViewModel.allTableExpense.observe(this) { lstExpense ->
                    if (lstExpense.isNotEmpty() && listItemReceived.isEmpty()) {
                        // Lọc danh sách expense với state = true
                        val filteredExpenseList = lstExpense.filter { it.status == Constants.NOT_RECEIVED }
                        listItemReceived.addAll(filteredExpenseList)

                        // Tạo bản đồ để tổng hợp các khoản thu nhập và chi tiêu theo năm
                        val incomeMap = mutableMapOf<String, Double>()
                        val expenseMap = mutableMapOf<String, Double>()

                        // Tổng hợp các khoản thu nhập theo năm
                        listItemPaid.forEach { item ->
                            val year = yearFormat.format(dateFormat.parse(item.date)!!)
                            incomeMap[year] = incomeMap.getOrDefault(year, 0.0) + item.amount
                        }

                        // Tổng hợp các khoản chi tiêu theo năm
                        listItemReceived.forEach { item ->
                            val year = yearFormat.format(dateFormat.parse(item.date)!!)
                            expenseMap[year] = expenseMap.getOrDefault(year, 0.0) + item.amount
                        }

                        // Lấy danh sách năm từ cả hai bản đồ
                        val allYears = incomeMap.keys union expenseMap.keys

                        allYears.forEach { year ->
                            val incomeAmount = incomeMap[year] ?: 0.0
                            val expenseAmount = expenseMap[year] ?: 0.0
                            matchingItems.add(
                                DateAmount(
                                    year,
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