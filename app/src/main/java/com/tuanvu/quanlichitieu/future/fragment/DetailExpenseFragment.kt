package com.tuanvu.quanlichitieu.future.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.tuanvu.quanlichitieu.R
import com.tuanvu.quanlichitieu.base.BaseFragment
import com.tuanvu.quanlichitieu.databinding.FragmentDetailExpenseBinding
import com.tuanvu.quanlichitieu.future.application.MyApplication
import com.tuanvu.quanlichitieu.future.database.entity.TableExpense
import com.tuanvu.quanlichitieu.future.database.viewmodel.ExpenseViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.ExpenseViewModelFactory
import com.tuanvu.quanlichitieu.future.dialog.DeleteDialog
import com.tuanvu.quanlichitieu.future.ultis.Constants

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

    override fun initView() {
        arguments?.let {
            incomeId = it.getLong(INCOME_ID)
        }

        expenseViewModel.getExpenseWithId(incomeId).observe(this) { item ->
            if (!::itemExpense.isInitialized) {
                itemExpense = item
                binding.inputIdCat.setText(item.expense_id.toString())
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
            itemExpense.category_id = binding.inputIdCat.text.toString().toLong()
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