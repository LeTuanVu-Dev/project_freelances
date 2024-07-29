package com.tuanvu.quanlichitieu.future.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.tuanvu.quanlichitieu.base.BaseFragment
import com.tuanvu.quanlichitieu.databinding.FragmentAddIncomeBinding
import com.tuanvu.quanlichitieu.databinding.FragmentAddIncomeCategoriesBinding
import com.tuanvu.quanlichitieu.future.application.MyApplication
import com.tuanvu.quanlichitieu.future.database.entity.Income
import com.tuanvu.quanlichitieu.future.database.entity.IncomeCategories
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeCategoriesViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeCategoriesViewModelFactory
import com.tuanvu.quanlichitieu.future.preferences.SharedPreferenceUtils
import com.tuanvu.quanlichitieu.future.ultis.AppExtensions
import com.tuanvu.quanlichitieu.future.ultis.Constants

class AddIncomeCatFragment :BaseFragment<FragmentAddIncomeCategoriesBinding>() {

    companion object {
        fun instance(): AddIncomeCatFragment {
            return newInstance(AddIncomeCatFragment::class.java)
        }
    }
    override fun initView() {
        binding.ivBack.setOnClickListener {
            handlerBackPressed()
        }

        binding.btnAdd.setOnClickListener {
            if (isValidAddDetails()){
                val des = binding.inputName.text.toString().trim()
                val itemIncomeCat = IncomeCategories(name = des)
                val result = Bundle().apply {
                    putSerializable("DATA_KEY_ADD", itemIncomeCat)
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
    ): FragmentAddIncomeCategoriesBinding {
        return FragmentAddIncomeCategoriesBinding.inflate(inflater,container,false)
    }
}