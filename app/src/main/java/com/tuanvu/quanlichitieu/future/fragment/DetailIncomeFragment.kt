package com.tuanvu.quanlichitieu.future.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tuanvu.quanlichitieu.R
import com.tuanvu.quanlichitieu.base.BaseFragment
import com.tuanvu.quanlichitieu.databinding.FragmentDetailIncomeBinding
import com.tuanvu.quanlichitieu.future.application.MyApplication
import com.tuanvu.quanlichitieu.future.database.entity.Income
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeCategoriesViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeCategoriesViewModelFactory
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeViewModelFactory
import com.tuanvu.quanlichitieu.future.dialog.DeleteDialog
import com.tuanvu.quanlichitieu.future.ultis.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

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
    private val incomeCategoriesViewModel: IncomeCategoriesViewModel by viewModels {
        IncomeCategoriesViewModelFactory((requireActivity().application as MyApplication).incomeCategoriesRepository)
    }
    private var listIDCategory = arrayListOf<Long>()
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


        lifecycleScope.launch {
            arguments?.let {
                incomeId = it.getLong(INCOME_ID)
            }
            withContext(Dispatchers.Main){
                incomeCategoriesViewModel.allTableIncomeCategories.observe(this@DetailIncomeFragment){
                        list ->
                    if (list.isNotEmpty() && listIDCategory.isEmpty()){
                        listIDCategory.addAll(list.map { it.category_id })
                        val adapter  = ArrayAdapter(requireContext(), R.layout.item_spinner, listIDCategory)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        // Bước 3: Gán ArrayAdapter cho Spinner
                        binding.inputIdCat.adapter = adapter
                        incomeViewModel.getIncomeWithId(incomeId).observe(this@DetailIncomeFragment) { item ->
                            if (!::itemIncome.isInitialized) {
                                itemIncome = item
                                Log.d("VuLT", "initView: "+listIDCategory.indexOf(item.category_id))
                                binding.inputIdCat.setSelection(listIDCategory.indexOf(item.category_id))
                                binding.inputAmount.setText(item.amount.toString())
                                binding.inputDate.text = item.date
                                binding.inputDescription.setText(item.description)
                                isState = item.status == Constants.RECEIVED
                                binding.ivState.setImageResource(if (item.status == Constants.RECEIVED) R.drawable.toggle_turn_on
                                else R.drawable.toggle_turn_off)
                            }
                        }
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
            binding.ivState.setImageResource(if (!isState) R.drawable.toggle_turn_off
            else R.drawable.toggle_turn_on)
        }

        binding.btnUpdate.setOnClickListener {
            itemIncome.date = binding.inputDate.text.toString().trim()
            itemIncome.description = binding.inputDescription.text.toString().trim()
            itemIncome.category_id = binding.inputIdCat.selectedItem.toString().toLong()
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