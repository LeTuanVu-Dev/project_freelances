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
import com.tuanvu.quanlichitieu.databinding.ActivityIncomeCategoriesBinding
import com.tuanvu.quanlichitieu.future.application.MyApplication
import com.tuanvu.quanlichitieu.future.database.entity.ExpenseCategories
import com.tuanvu.quanlichitieu.future.database.entity.IncomeCategories
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeCategoriesViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeCategoriesViewModelFactory
import com.tuanvu.quanlichitieu.future.dialog.DeleteDialog
import com.tuanvu.quanlichitieu.future.dialog.RenameDialog
import com.tuanvu.quanlichitieu.future.epoxy.controller.IncomeCategoriesController
import com.tuanvu.quanlichitieu.future.fragment.AddExpenseFragment
import com.tuanvu.quanlichitieu.future.fragment.AddIncomeCatFragment
import com.tuanvu.quanlichitieu.future.fragment.DetailIncomeFragment
import com.tuanvu.quanlichitieu.future.ultis.AppExtensions
import com.tuanvu.quanlichitieu.future.ultis.makeGone
import com.tuanvu.quanlichitieu.future.ultis.makeVisible

class IncomeCategoriesActivity : BaseActivity<ActivityIncomeCategoriesBinding>() {
    override fun getViewBinding(): ActivityIncomeCategoriesBinding {
        return ActivityIncomeCategoriesBinding.inflate(layoutInflater)
    }

    private var listIncomeCategories = arrayListOf<IncomeCategories>()
    private var filteredList = arrayListOf<IncomeCategories>()

    private var incomeCategoriesController = IncomeCategoriesController()
    private lateinit var itemIncome: IncomeCategories
    private var isLoadData = false

    private val incomeCategoriesViewModel: IncomeCategoriesViewModel by viewModels {
        IncomeCategoriesViewModelFactory((application as MyApplication).incomeCategoriesRepository)
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
            addFragment(AddIncomeCatFragment.instance())
        }
        binding.tvCancel.setOnClickListener {
            binding.edtInputSearch.setText("")
            binding.lnSearch.makeGone()
            binding.ivSearch.makeVisible()
            incomeCategoriesController.setDataListItem(listIncomeCategories)
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
            val result = bundle.getSerializable("DATA_KEY_ADD") as IncomeCategories
            listIncomeCategories.add(result)
            incomeCategoriesController.setDataItem(result)
            incomeCategoriesViewModel.insert(result)
            if (listIncomeCategories.isEmpty()) {
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
            val result = bundle.getSerializable("DATA_KEY_DELETE") as IncomeCategories
//            incomeViewModel.delete(result)
            incomeCategoriesController.deleteAudioAndUpdateList(result)
            listIncomeCategories.remove(result)
            if (listIncomeCategories.isEmpty()) {
                binding.lnEmpty.makeVisible()
                binding.epxListIncome.makeGone()
            }
        }
        supportFragmentManager.setFragmentResultListener(
            "KEY_UPDATE",
            this
        ) { _: String, bundle: Bundle ->
            val result = bundle.getSerializable("DATA_KEY_UPDATE") as IncomeCategories
            incomeCategoriesViewModel.update(result)
            val index = listIncomeCategories.indexOfFirst { it.category_id == result.category_id }

            if (index != -1) {
                // Thay thế phần tử tại chỉ số đó
                listIncomeCategories[index] = result
            }
            incomeCategoriesController.updateItemUpdateList(result)
        }

        incomeCategoriesViewModel.allTableIncomeCategories.observe(this) { list ->
            if (!isLoadData) {
                listIncomeCategories.addAll(list)
                isLoadData = true
                incomeCategoriesController.setDataListItem(listIncomeCategories)
                binding.epxListIncome.setControllerAndBuildModels(incomeCategoriesController)
                incomeCategoriesController.setOnClick {
                }
                incomeCategoriesController.setOnClickMore { audioHistory, view ->
                    showPopupMenu(view, audioHistory)
                }
                if (listIncomeCategories.isEmpty()) {
                    binding.lnEmpty.makeVisible()
                    binding.epxListIncome.makeGone()
                }
            }
        }
    }

    private fun filterWithSearch(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(listIncomeCategories)
        } else {
            val lowerCaseQuery = query.lowercase()
            for (item in listIncomeCategories) {
                if (item.name.lowercase().contains(lowerCaseQuery)) {
                    filteredList.add(item)
                }
            }
        }
        incomeCategoriesController.setDataListItem(filteredList)
    }

    @SuppressLint("MissingInflatedId")
    private fun showPopupMenu(view: View, income: IncomeCategories) {
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

    private fun showDialogRename(incomeCategories: IncomeCategories) {
        val renameDialog = RenameDialog(this) { name ->
            if (name.isNotEmpty()) {
                incomeCategories.name = name
                incomeCategoriesViewModel.update(incomeCategories)
                incomeCategoriesController.requestModelBuild()
            } else {
                AppExtensions.showToast(this, getString(R.string.name_cannot_be_blank))
            }

        }

        if (!renameDialog.isShowing) {
            incomeCategories.name.let { renameDialog.setTextCurrentName(it) }
            renameDialog.show()
        }
    }

    private fun showDialogConfirmDelete(income: IncomeCategories) {
        val deleteDialog = DeleteDialog(this) {
            incomeCategoriesViewModel.delete(income)
            listIncomeCategories.remove(income)
            if (listIncomeCategories.isEmpty()) {
                binding.lnEmpty.makeVisible()
                binding.epxListIncome.makeGone()
            }
            incomeCategoriesController.deleteAudioAndUpdateList(income)
        }
        if (!deleteDialog.isShowing) {
            deleteDialog.show()
        }
    }
}