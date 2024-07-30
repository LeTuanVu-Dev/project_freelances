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
import com.tuanvu.quanlichitieu.future.epoxy.controller.IncomeCategoriesController
import com.tuanvu.quanlichitieu.future.epoxy.controller.SumController
import com.tuanvu.quanlichitieu.future.ultis.BarChartView
import com.tuanvu.quanlichitieu.future.ultis.Constants
import com.tuanvu.quanlichitieu.future.ultis.DateAmount

class StaticDayFragment : BaseFragment<FragmentStaticsDayBinding>() {
    companion object {
        fun instance(): StaticDayFragment {
            return newInstance(StaticDayFragment::class.java)
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

        incomeViewModel.allTableIncome.observe(this) { lstIncome ->
            if (lstIncome.isNotEmpty() && listItemPaid.isEmpty()) {
                // Lọc danh sách income với state = true
                val filteredIncomeList = lstIncome.filter { it.status == Constants.PAID }
                listItemPaid.addAll(filteredIncomeList)

                expenseViewModel.allTableExpense.observe(this) { lstExpense ->
                    if (lstExpense.isNotEmpty() && listItemReceived.isEmpty()) {
                        // Lọc danh sách expense với state = true
                        val filteredExpenseList =
                            lstExpense.filter { it.status == Constants.RECEIVED }
                        listItemReceived.addAll(filteredExpenseList)

                        val incomeDates = listItemPaid.map { it.date }.toSet()
                        val expenseDates = listItemReceived.map { it.date }.toSet()
                        val allDates = incomeDates union expenseDates

                        allDates.forEach { date ->
                            val incomeAmount = listItemPaid.find { it.date == date }?.amount ?: 0.0
                            val expenseAmount =
                                listItemReceived.find { it.date == date }?.amount ?: 0.0
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
                                            item.incomeAmount, // Random value between 0 and 100
                                            item.expenseAmount, // Random value between 0 and 100
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