package com.tuanvu.quanlichitieu.future.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.tuanvu.quanlichitieu.R
import com.tuanvu.quanlichitieu.base.BaseFragment
import com.tuanvu.quanlichitieu.databinding.FragmentDetailExpenseBinding
import com.tuanvu.quanlichitieu.future.application.MyApplication
import com.tuanvu.quanlichitieu.future.database.entity.TableExpense
import com.tuanvu.quanlichitieu.future.database.viewmodel.ExpenseCategoriesViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.ExpenseCategoriesViewModelFactory
import com.tuanvu.quanlichitieu.future.database.viewmodel.ExpenseViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.ExpenseViewModelFactory
import com.tuanvu.quanlichitieu.future.dialog.DeleteDialog
import com.tuanvu.quanlichitieu.future.ultis.Constants
import java.util.Calendar

class DetailExpenseFragment : BaseFragment<FragmentDetailExpenseBinding>() {
    companion object {
        private const val INCOME_ID = "INCOME_ID"
        fun instance(audioId: Long): DetailExpenseFragment {
            Bundle().apply {
                putLong(INCOME_ID, audioId)
                return newInstance(DetailExpenseFragment::class.java, this)

            }
        }
    }

    private lateinit var itemExpense: TableExpense
    private var incomeId: Long = -1
    private var isState = false
    private val expenseViewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory((requireActivity().application as MyApplication).expenseRepository)
    }
    private var listIDCategory = arrayListOf<Long>()
    private val expenseCategoriesViewModel: ExpenseCategoriesViewModel by viewModels {
        ExpenseCategoriesViewModelFactory((requireActivity().application as MyApplication).expenseCategoriesRepository)
    }
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            binding.inputDate.text = selectedDate
        }, year, month, day)

        datePickerDialog.show()
    }
    override fun initView() {
        arguments?.let {
            incomeId = it.getLong(INCOME_ID)
        }
        expenseCategoriesViewModel.allExpenseCategories.observe(this){
                list ->
            if (list.isNotEmpty() && listIDCategory.isEmpty()){
                listIDCategory.addAll(list.map { it.category_id })
                val adapter  = ArrayAdapter(requireContext(), R.layout.item_spinner, listIDCategory)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                // Bước 3: Gán ArrayAdapter cho Spinner
                binding.inputIdCat.adapter = adapter
                expenseViewModel.getExpenseWithId(incomeId).observe(this) { item ->
                    if (!::itemExpense.isInitialized) {
                        itemExpense = item
                        binding.inputIdCat.setSelection(item.expense_id.toInt())
                        binding.inputAmount.setText(item.amount.toString())
                        binding.inputDate.setText(item.date)
                        binding.inputDescription.setText(item.description)
                        isState = item.status == Constants.PAID
                        binding.ivState.setImageResource(
                            if (item.status == Constants.PAID) R.drawable.toggle_turn_off
                            else R.drawable.toggle_turn_on
                        )
                    }
                }

            }
        }

        binding.inputDate.setOnClickListener {
            showDatePickerDialog()
        }

        binding.ivBack.setOnClickListener {
            handlerBackPressed()
        }
        binding.ivDelete.setOnClickListener {
            showDialogConfirmDelete()
        }
        binding.ivState.setOnClickListener {
            isState = !isState
            binding.ivState.setImageResource(
                if (isState) R.drawable.toggle_turn_off
                else R.drawable.toggle_turn_on
            )
        }

        binding.btnUpdate.setOnClickListener {
            itemExpense.date = binding.inputDate.text.toString().trim()
            itemExpense.description = binding.inputDescription.text.toString().trim()
            itemExpense.category_id = binding.inputIdCat.selectedItem.toString().toLong()
            itemExpense.amount = binding.inputAmount.text.toString().toFloat()
            itemExpense.status = if (isState) Constants.PAID else Constants.UNPAID
            expenseViewModel.update(itemExpense)
            val result = Bundle().apply {
                putSerializable("DATA_KEY_UPDATE", itemExpense)
            }
            setFragmentResult("KEY_UPDATE", result)
            handlerBackPressed()
        }
    }

    private fun showDialogConfirmDelete() {
        val deleteDialog = DeleteDialog(requireContext()) {
            val result = Bundle().apply {
                putSerializable("DATA_KEY_DELETE", itemExpense)
            }
            setFragmentResult("KEY_DELETE", result)
            handlerBackPressed()
        }
        if (!deleteDialog.isShowing) {
            deleteDialog.show()
        }
    }

    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment(this)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailExpenseBinding {
        return FragmentDetailExpenseBinding.inflate(inflater, container, false)
    }
}