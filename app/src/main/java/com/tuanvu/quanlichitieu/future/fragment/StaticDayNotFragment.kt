package com.tuanvu.quanlichitieu.future.fragment

import android.util.Log
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

class StaticDayNotFragment : BaseFragment<FragmentStaticsDayBinding>() {
    companion object {
        fun instance(): StaticDayNotFragment {
            return newInstance(StaticDayNotFragment::class.java)
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

    private var sumController = SumController()

    private val matchingItems = arrayListOf<DateAmount>()
    override fun initView() {
        Log.d("Cuong", "initView:")
        binding.tvCost.text = "(Nghìn Đồng)"
        binding.tvTimer.text = "(Ngày)"
        incomeViewModel.allTableIncome.observe(this) { lstIncome ->
            if (lstIncome.isNotEmpty() && listItemPaid.isEmpty()) {
                // Lọc danh sách income với state = true
                val filteredIncomeList = lstIncome.filter { it.status == Constants.NOT_RECEIVED }
                listItemPaid.addAll(filteredIncomeList)

                expenseViewModel.allTableExpense.observe(this) { lstExpense ->
                    if (lstExpense.isNotEmpty() && listItemReceived.isEmpty()) {
                        // Lọc danh sách expense với state = true
                        val filteredExpenseList = lstExpense.filter { it.status == Constants.UNPAID }
                        listItemReceived.addAll(filteredExpenseList)

                        val incomeMap = mutableMapOf<String, Double>()
                        val expenseMap = mutableMapOf<String, Double>()

                        // Gộp các khoản thu nhập theo ngày
                        listItemPaid.forEach { item ->
                            val amount = incomeMap.getOrDefault(item.date, 0.0)
                            incomeMap[item.date] = amount + item.amount
                        }

                        // Gộp các khoản chi tiêu theo ngày
                        listItemReceived.forEach { item ->
                            val amount = expenseMap.getOrDefault(item.date, 0.0)
                            expenseMap[item.date] = amount + item.amount
                        }

                        val allDates = incomeMap.keys union expenseMap.keys

                        allDates.forEach { date ->
                            val incomeAmount = incomeMap[date] ?: 0.0
                            val expenseAmount = expenseMap[date] ?: 0.0
                            matchingItems.add(
                                DateAmount(
                                    date.split("/").first(),
                                    incomeAmount.toFloat(),
                                    expenseAmount.toFloat()
                                )
                            )
                        }

                        sumController.setDataListItem(matchingItems)
                        binding.epxList.setControllerAndBuildModels(sumController)

                        // Ví dụ: In ra các giá trị trong danh sách matchingItems
                        binding.pieChart
                            .submitData(mutableListOf<BarChartView.DataChart>().apply {
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
        return FragmentStaticsDayBinding.inflate(inflater, container, false)
    }
}