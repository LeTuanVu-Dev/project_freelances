package com.tuanvu.quanlichitieu.future.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.tuanvu.quanlichitieu.R
import com.tuanvu.quanlichitieu.base.BaseFragment
import com.tuanvu.quanlichitieu.databinding.FragmentDetailIncomeBinding
import com.tuanvu.quanlichitieu.future.application.MyApplication
import com.tuanvu.quanlichitieu.future.database.entity.Income
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeViewModelFactory
import com.tuanvu.quanlichitieu.future.dialog.DeleteDialog
import com.tuanvu.quanlichitieu.future.ultis.Constants

class DetailIncomeFragment :BaseFragment<FragmentDetailIncomeBinding>() {
    companion object {
        private const val INCOME_ID = "INCOME_ID"
        fun instance(audioId: Long): DetailIncomeFragment {
            Bundle().apply {
                putLong(INCOME_ID, audioId)
                return newInstance(DetailIncomeFragment::class.java, this)

            }
        }
    }
    private lateinit var itemIncome:Income
    private var incomeId: Long = -1
    private var isState = false
    private val incomeViewModel: IncomeViewModel by viewModels {
        IncomeViewModelFactory((requireActivity().application as MyApplication).incomeRepository)
    }
    override fun initView() {
        arguments?.let {
            incomeId = it.getLong(INCOME_ID)
        }

        incomeViewModel.getIncomeWithId(incomeId).observe(this) { item ->
            if (!::itemIncome.isInitialized) {
                itemIncome = item
                binding.inputIdCat.setText(item.income_id.toString())
                binding.inputAmount.setText(item.amount.toString())
                binding.inputDate.setText(item.date)
                binding.inputDescription.setText(item.description)
                isState = item.status == Constants.RECEIVED
                binding.ivState.setImageResource(if (item.status == Constants.RECEIVED) R.drawable.toggle_turn_off
                else R.drawable.toggle_turn_on)
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
            binding.ivState.setImageResource(if (isState) R.drawable.toggle_turn_off
            else R.drawable.toggle_turn_on)
        }

        binding.btnUpdate.setOnClickListener {
            itemIncome.date = binding.inputDate.text.toString().trim()
            itemIncome.description = binding.inputDescription.text.toString().trim()
            itemIncome.category_id = binding.inputIdCat.text.toString().toLong()
            itemIncome.amount = binding.inputAmount.text.toString().toFloat()
            itemIncome.status = if (isState) Constants.RECEIVED else  Constants.NOT_RECEIVED
            incomeViewModel.update(itemIncome)
            val result = Bundle().apply {
                putSerializable("DATA_KEY_UPDATE", itemIncome)
            }
            setFragmentResult("KEY_UPDATE", result)
            handlerBackPressed()
        }
    }
    private fun showDialogConfirmDelete() {
        val deleteDialog = DeleteDialog(requireContext()) {
            val result = Bundle().apply {
                putSerializable("DATA_KEY_DELETE", itemIncome)
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
    ): FragmentDetailIncomeBinding {
        return FragmentDetailIncomeBinding.inflate(inflater,container,false)
    }
}