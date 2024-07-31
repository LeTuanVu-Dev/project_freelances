package com.tuanvu.quanlichitieu.future.fragment

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.tuanvu.quanlichitieu.R
import com.tuanvu.quanlichitieu.base.BaseFragment
import com.tuanvu.quanlichitieu.databinding.FragmentAddIncomeBinding
import com.tuanvu.quanlichitieu.future.application.MyApplication
import com.tuanvu.quanlichitieu.future.database.entity.Income
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeCategoriesViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeCategoriesViewModelFactory
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeViewModelFactory
import com.tuanvu.quanlichitieu.future.preferences.SharedPreferenceUtils
import com.tuanvu.quanlichitieu.future.service.AlarmReceiver
import com.tuanvu.quanlichitieu.future.ultis.AppExtensions.showToast
import com.tuanvu.quanlichitieu.future.ultis.Constants
import java.util.Calendar

class AddIncomeFragment :BaseFragment<FragmentAddIncomeBinding>() {
    companion object {
        fun instance(): AddIncomeFragment {
            return newInstance(AddIncomeFragment::class.java)
        }
    }
    private var isState = false

    private var listIDCategory = arrayListOf<Long>()

    private val incomeCategoriesViewModel: IncomeCategoriesViewModel by viewModels {
        IncomeCategoriesViewModelFactory((requireActivity().application as MyApplication).incomeCategoriesRepository)
    }
    override fun initView() {
        incomeCategoriesViewModel.allTableIncomeCategories.observe(this){
            list ->
            if (list.isNotEmpty() && listIDCategory.isEmpty()){
                listIDCategory.addAll(list.map { it.category_id })
                val adapter  = ArrayAdapter(requireContext(), R.layout.item_spinner, listIDCategory)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                // Bước 3: Gán ArrayAdapter cho Spinner
                binding.inputIdCat.adapter = adapter
            }
        }
        binding.ivState.setOnClickListener {
            isState = !isState
            binding.ivState.setImageResource(if (isState) R.drawable.toggle_turn_on else R.drawable.toggle_turn_off )
        }
        binding.ivBack.setOnClickListener {
            handlerBackPressed()
        }

        binding.inputDate.setOnClickListener {
            showDatePickerDialog()
        }

        binding.btnAdd.setOnClickListener {
            if (isValidAddDetails()){
                val idCat = binding.inputIdCat.selectedItem.toString().toLong()
                val amount = binding.inputAmount.text.toString().toFloat()
                val date = binding.inputDate.text.toString().trim()
                val des = binding.inputDescription.text.toString().trim()
                val status = if (isState) Constants.RECEIVED else Constants.NOT_RECEIVED
                val itemIncome = Income(user_id = SharedPreferenceUtils.keyUserLogin,
                    category_id =idCat, amount = amount, date = date, description = des , status = status)
                val result = Bundle().apply {
                    putSerializable("DATA_KEY_ADD", itemIncome)
                }
                setFragmentResult("KEY_ADD", result)
                handlerBackPressed()
            }
        }
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
    private fun isValidAddDetails(): Boolean {
        // email trống
        return if (binding.inputIdCat.selectedItem == null) {
            showToast(requireContext(),"Bạn chưa có category nào")
            false
        } else if (binding.inputAmount.text.toString().trim().isEmpty()) {
            showToast(requireContext(),"Amount không được trống")
            false
        } else if (binding.inputDate.text.toString().trim().isEmpty()) {
            showToast(requireContext(),"Date không được trống")
            false
        } else if (binding.inputDescription.text.toString().trim().isEmpty()) {
            showToast(requireContext(),"Description không được trống")
            false
        } else {
            true
        }
    }



    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment(this)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddIncomeBinding {
        return FragmentAddIncomeBinding.inflate(inflater,container,false)
    }
}