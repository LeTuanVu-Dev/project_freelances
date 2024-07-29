package com.tuanvu.quanlichitieu.future.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.activity.viewModels
import com.tuanvu.quanlichitieu.R
import com.tuanvu.quanlichitieu.base.BaseActivity
import com.tuanvu.quanlichitieu.databinding.ActivityExpenseBinding
import com.tuanvu.quanlichitieu.future.application.MyApplication
import com.tuanvu.quanlichitieu.future.database.entity.Income
import com.tuanvu.quanlichitieu.future.database.entity.TableExpense
import com.tuanvu.quanlichitieu.future.database.viewmodel.ExpenseViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.ExpenseViewModelFactory
import com.tuanvu.quanlichitieu.future.dialog.DeleteDialog
import com.tuanvu.quanlichitieu.future.epoxy.controller.ExpenseController
import com.tuanvu.quanlichitieu.future.fragment.AddExpenseFragment
import com.tuanvu.quanlichitieu.future.fragment.AddIncomeFragment
import com.tuanvu.quanlichitieu.future.fragment.DetailExpenseFragment
import com.tuanvu.quanlichitieu.future.fragment.DetailIncomeFragment
import com.tuanvu.quanlichitieu.future.preferences.SharedPreferenceUtils
import com.tuanvu.quanlichitieu.future.ultis.makeGone
import com.tuanvu.quanlichitieu.future.ultis.makeVisible

class ExpenseActivity : BaseActivity<ActivityExpenseBinding>() {
    override fun getViewBinding(): ActivityExpenseBinding {
        return ActivityExpenseBinding.inflate(layoutInflater)
    }
    private var listExpense = arrayListOf<TableExpense>()
    private var filteredList = arrayListOf<TableExpense>()

    private var expenseController = ExpenseController()
    private lateinit var itemExpense: TableExpense
    private var isLoadData = false

    private val expenseViewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory((application as MyApplication).expenseRepository)
    }
    override fun createView() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.ivSearch.setOnClickListener {
            binding.ivSearch.makeGone()
            binding.lnSearch.makeVisible()
        }
        binding.ivAdd.setOnClickListener {
            addFragment(AddExpenseFragment.instance())
        }
        binding.tvCancel.setOnClickListener {
            binding.edtInputSearch.setText("")
            binding.lnSearch.makeGone()
            binding.ivSearch.makeVisible()
            expenseController.setDataListItem(listExpense)
        }

        binding.edtInputSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterWithSearch(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        supportFragmentManager.setFragmentResultListener(
            "KEY_ADD",
            this
        ) { _: String, bundle: Bundle ->
            val result = bundle.getSerializable("DATA_KEY_ADD") as TableExpense
            listExpense.add(result)
            expenseViewModel.insert(result)
            expenseController.setDataItem(result)
            if (listExpense.isEmpty()) {
                binding.lnEmpty.makeVisible()
                binding.epxListIncome.makeGone()
            }
            else{
                binding.lnEmpty.makeGone()
                binding.epxListIncome.makeVisible()
            }
        }

        supportFragmentManager.setFragmentResultListener(
            "KEY_DELETE",
            this
        ) { _: String, bundle: Bundle ->
            val result = bundle.getSerializable("DATA_KEY_DELETE") as TableExpense
//            incomeViewModel.delete(result)
            expenseController.deleteAudioAndUpdateList(result)
            listExpense.remove(result)
            if (listExpense.isEmpty()) {
                binding.lnEmpty.makeVisible()
                binding.epxListIncome.makeGone()
            }
        }
        supportFragmentManager.setFragmentResultListener(
            "KEY_UPDATE",
            this
        ) { _: String, bundle: Bundle ->
            val result = bundle.getSerializable("DATA_KEY_UPDATE") as TableExpense
            expenseViewModel.update(result)
            val index = listExpense.indexOfFirst { it.expense_id == result.expense_id }

            if (index != -1) {
                // Thay thế phần tử tại chỉ số đó
                listExpense[index] = result
            }
            expenseController.updateItemUpdateList(result)
        }

        expenseViewModel.getExpenseWithUserId(SharedPreferenceUtils.keyUserLogin).observe(this) { list ->
            if (!isLoadData) {
                listExpense.addAll(list)
                isLoadData = true
                expenseController.setDataListItem(listExpense)
                binding.epxListIncome.setControllerAndBuildModels(expenseController)
                expenseController.setOnClick { expense ->
                    itemExpense = expense
                    addFragment(DetailExpenseFragment.instance(expense.expense_id))

                }
                expenseController.setOnClickMore { expense, view ->
                    showPopupMenu(view, expense)
                }
                if (listExpense.isEmpty()) {
                    binding.lnEmpty.makeVisible()
                    binding.epxListIncome.makeGone()
                }
            }
        }
    }
    private fun filterWithSearch(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(listExpense)
        } else {
            val lowerCaseQuery = query.lowercase()
            for (item in listExpense) {
                if (item.description.lowercase().contains(lowerCaseQuery)) {
                    filteredList.add(item)
                }
            }
        }
        expenseController.setDataListItem(filteredList)
    }
    @SuppressLint("MissingInflatedId")
    private fun showPopupMenu(view: View, expense: TableExpense) {
        val viewGroup = LinearLayout(this)
        val layoutInflater =
            this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = layoutInflater.inflate(R.layout.layout_popup_menu, viewGroup)
        val popupMenu = PopupWindow(this)
        popupMenu.contentView = layout
        popupMenu.width = LinearLayout.LayoutParams.WRAP_CONTENT
        popupMenu.height = LinearLayout.LayoutParams.WRAP_CONTENT
        popupMenu.isFocusable = false
        popupMenu.isOutsideTouchable = true

        popupMenu.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        layout.findViewById<LinearLayout>(R.id.ll_item_update).setOnClickListener {
//            showDialogRename(income)
            addFragment(DetailExpenseFragment.instance(expense.expense_id))
            popupMenu.dismiss()
        }
        layout.findViewById<LinearLayout>(R.id.ll_item_delete).setOnClickListener {
            showDialogConfirmDelete(expense)
            popupMenu.dismiss()
        }

        popupMenu.showAsDropDown(view, -200, 0, Gravity.NO_GRAVITY)
    }
    private fun showDialogConfirmDelete(expense: TableExpense) {
        val deleteDialog = DeleteDialog(this) {
            expenseViewModel.delete(expense)
            listExpense.remove(expense)
            if (listExpense.isEmpty()) {
                binding.lnEmpty.makeVisible()
                binding.epxListIncome.makeGone()
            }
            expenseController.deleteAudioAndUpdateList(expense)
        }
        if (!deleteDialog.isShowing) {
            deleteDialog.show()
        }
    }
}