package com.tuanvu.quanlichitieu.future.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.tuanvu.quanlichitieu.base.BaseFragment
import com.tuanvu.quanlichitieu.databinding.FragmentAddExpenseCategoriesBinding
import com.tuanvu.quanlichitieu.future.application.MyApplication
import com.tuanvu.quanlichitieu.future.database.entity.ExpenseCategories
import com.tuanvu.quanlichitieu.future.database.viewmodel.ExpenseCategoriesViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.ExpenseCategoriesViewModelFactory
import com.tuanvu.quanlichitieu.future.ultis.AppExtensions

class AddExpenseCatFragment : BaseFragment<FragmentAddExpenseCategoriesBinding>() {

    companion object {
        fun instance(): AddExpenseCatFragment {
            return newInstance(AddExpenseCatFragment::class.java)
        }
    }

    override fun initView() {
        binding.ivBack.setOnClickListener {
            handlerBackPressed()
        }

        binding.btnAdd.setOnClickListener {
            if (isValidAddDetails()) {
                val des = binding.inputName.text.toString().trim()
                val itemExpenseCat = ExpenseCategories(name = des)
                val result = Bundle().apply {
                    putSerializable("DATA_KEY_ADD", itemExpenseCat)
                }
                setFragmentResult("KEY_ADD", result)
                handlerBackPressed()
            }
        }
    }
    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment(this)
    }

    private fun isValidAddDetails(): Boolean {
        // email trống
        return if (binding.inputName.text.toString().trim().isEmpty()) {
            AppExtensions.showToast(requireContext(), "Enter Name")
            false
        } else {
            true
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddExpenseCategoriesBinding {
        return FragmentAddExpenseCategoriesBinding.inflate(inflater, container, false)
    }
}