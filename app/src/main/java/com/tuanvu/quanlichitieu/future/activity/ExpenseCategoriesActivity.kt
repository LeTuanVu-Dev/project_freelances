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
import com.tuanvu.quanlichitieu.databinding.ActivityExpenseCategoriesBinding
import com.tuanvu.quanlichitieu.future.application.MyApplication
import com.tuanvu.quanlichitieu.future.database.entity.ExpenseCategories
import com.tuanvu.quanlichitieu.future.database.entity.IncomeCategories
import com.tuanvu.quanlichitieu.future.database.entity.TableExpense
import com.tuanvu.quanlichitieu.future.database.viewmodel.ExpenseCategoriesViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.ExpenseCategoriesViewModelFactory
import com.tuanvu.quanlichitieu.future.dialog.DeleteDialog
import com.tuanvu.quanlichitieu.future.dialog.RenameDialog
import com.tuanvu.quanlichitieu.future.epoxy.controller.ExpenseCategoriesController
import com.tuanvu.quanlichitieu.future.fragment.AddExpenseCatFragment
import com.tuanvu.quanlichitieu.future.fragment.AddExpenseFragment
import com.tuanvu.quanlichitieu.future.ultis.AppExtensions
import com.tuanvu.quanlichitieu.future.ultis.makeGone
import com.tuanvu.quanlichitieu.future.ultis.makeVisible

class ExpenseCategoriesActivity : BaseActivity<ActivityExpenseCategoriesBinding>() {
    override fun getViewBinding(): ActivityExpenseCategoriesBinding {
        return ActivityExpenseCategoriesBinding.inflate(layoutInflater)
    }

    private var listExpenseCategories = arrayListOf<ExpenseCategories>()
    private var filteredList = arrayListOf<ExpenseCategories>()

    private var expenseCategoriesController = ExpenseCategoriesController()
    private var isLoadData = false

    private val expenseCategoriesViewModel: ExpenseCategoriesViewModel by viewModels {
        ExpenseCategoriesViewModelFactory((application as MyApplication).expenseCategoriesRepository)
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
            addFragment(AddExpenseCatFragment.instance())
        }
        binding.tvCancel.setOnClickListener {
            binding.edtInputSearch.setText("")
            binding.lnSearch.makeGone()
            binding.ivSearch.makeVisible()
            expenseCategoriesController.setDataListItem(listExpenseCategories)
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
            val result = bundle.getSerializable("DATA_KEY_ADD") as ExpenseCategories
            listExpenseCategories.add(result)
            expenseCategoriesController.setDataItem(result)
            expenseCategoriesViewModel.insert(result)
            if (listExpenseCategories.isEmpty()) {
                binding.lnEmpty.makeVisible()
                binding.epxListIncome.makeGone()
            }
        }

        supportFragmentManager.setFragmentResultListener(
            "KEY_DELETE",
            this
        ) { _: String, bundle: Bundle ->
            val result = bundle.getSerializable("DATA_KEY_DELETE") as ExpenseCategories
//            incomeViewModel.delete(result)
            expenseCategoriesController.deleteAudioAndUpdateList(result)
            listExpenseCategories.remove(result)
            if (listExpenseCategories.isEmpty()) {
                binding.lnEmpty.makeVisible()
                binding.epxListIncome.makeGone()
            }
        }
        supportFragmentManager.setFragmentResultListener(
            "KEY_UPDATE",
            this
        ) { _: String, bundle: Bundle ->
            val result = bundle.getSerializable("DATA_KEY_UPDATE") as ExpenseCategories
            expenseCategoriesViewModel.update(result)
            val index = listExpenseCategories.indexOfFirst { it.category_id == result.category_id }

            if (index != -1) {
                // Thay thế phần tử tại chỉ số đó
                listExpenseCategories[index] = result
            }
            expenseCategoriesController.updateItemUpdateList(result)
        }

        expenseCategoriesViewModel.allExpenseCategories.observe(this) { list ->
            if (!isLoadData) {
                listExpenseCategories.addAll(list)
                isLoadData = true
                expenseCategoriesController.setDataListItem(listExpenseCategories)
                binding.epxListIncome.setControllerAndBuildModels(expenseCategoriesController)
                expenseCategoriesController.setOnClick {
                }
                expenseCategoriesController.setOnClickMore { audioHistory, view ->
                    showPopupMenu(view, audioHistory)
                }
                if (listExpenseCategories.isEmpty()) {
                    binding.lnEmpty.makeVisible()
                    binding.epxListIncome.makeGone()
                }
            }
        }
    }

    private fun filterWithSearch(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(listExpenseCategories)
        } else {
            val lowerCaseQuery = query.lowercase()
            for (item in listExpenseCategories) {
                if (item.name.lowercase().contains(lowerCaseQuery)) {
                    filteredList.add(item)
                }
            }
        }
        expenseCategoriesController.setDataListItem(filteredList)
    }

    @SuppressLint("MissingInflatedId")
    private fun showPopupMenu(view: View, income: ExpenseCategories) {
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
            showDialogRename(income)
            popupMenu.dismiss()
        }
        layout.findViewById<LinearLayout>(R.id.ll_item_delete).setOnClickListener {
            showDialogConfirmDelete(income)
            popupMenu.dismiss()
        }

        popupMenu.showAsDropDown(view, -200, 0, Gravity.NO_GRAVITY)
    }

    private fun showDialogRename(incomeCategories: ExpenseCategories) {
        val renameDialog = RenameDialog(this) { name ->
            if (name.isNotEmpty()) {
                incomeCategories.name = name
                expenseCategoriesViewModel.update(incomeCategories)
                expenseCategoriesController.requestModelBuild()
            } else {
                AppExtensions.showToast(this, getString(R.string.name_cannot_be_blank))
            }

        }

        if (!renameDialog.isShowing) {
            incomeCategories.name.let { renameDialog.setTextCurrentName(it) }
            renameDialog.show()
        }
    }

    private fun showDialogConfirmDelete(income: ExpenseCategories) {
        val deleteDialog = DeleteDialog(this) {
            expenseCategoriesViewModel.delete(income)
            listExpenseCategories.remove(income)
            if (listExpenseCategories.isEmpty()) {
                binding.lnEmpty.makeVisible()
                binding.epxListIncome.makeGone()
            }
            expenseCategoriesController.deleteAudioAndUpdateList(income)
        }
        if (!deleteDialog.isShowing) {
            deleteDialog.show()
        }
    }
}