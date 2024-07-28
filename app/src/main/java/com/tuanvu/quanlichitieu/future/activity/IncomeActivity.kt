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
import com.tuanvu.quanlichitieu.databinding.ActivityIncomeBinding
import com.tuanvu.quanlichitieu.databinding.ActivitySettingBinding
import com.tuanvu.quanlichitieu.future.application.MyApplication
import com.tuanvu.quanlichitieu.future.database.entity.Income
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeViewModelFactory
import com.tuanvu.quanlichitieu.future.database.viewmodel.TableUserViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.TableUserViewModelFactory
import com.tuanvu.quanlichitieu.future.dialog.DeleteDialog
import com.tuanvu.quanlichitieu.future.epoxy.controller.IncomeController
import com.tuanvu.quanlichitieu.future.fragment.DetailIncomeFragment
import com.tuanvu.quanlichitieu.future.preferences.SharedPreferenceUtils
import com.tuanvu.quanlichitieu.future.ultis.makeGone
import com.tuanvu.quanlichitieu.future.ultis.makeVisible

class IncomeActivity : BaseActivity<ActivityIncomeBinding>() {
    override fun getViewBinding(): ActivityIncomeBinding {
        return ActivityIncomeBinding.inflate(layoutInflater)
    }
    private var listIncome = arrayListOf<Income>()
    private var filteredList = arrayListOf<Income>()

    private var incomeController = IncomeController()
    private lateinit var itemIncome: Income
    private var isLoadData = false

    private val incomeViewModel: IncomeViewModel by viewModels {
        IncomeViewModelFactory((application as MyApplication).incomeRepository)
    }
    override fun createView() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.ivSearch.setOnClickListener {
            binding.ivSearch.makeGone()
            binding.lnSearch.makeVisible()
        }

        binding.tvCancel.setOnClickListener {
            binding.edtInputSearch.setText("")
            binding.lnSearch.makeGone()
            binding.ivSearch.makeVisible()
            incomeController.setDataListItem(listIncome)
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
            "KEY_DELETE",
            this
        ) { _: String, bundle: Bundle ->
            val result = bundle.getSerializable("DATA_KEY_DELETE") as Income
//            incomeViewModel.delete(result)
            incomeController.deleteAudioAndUpdateList(result)
            listIncome.remove(result)
            if (listIncome.isEmpty()) {
                binding.lnEmpty.makeVisible()
                binding.epxListIncome.makeGone()
            }
        }
        supportFragmentManager.setFragmentResultListener(
            "KEY_UPDATE",
            this
        ) { _: String, bundle: Bundle ->
            val result = bundle.getSerializable("DATA_KEY_UPDATE") as Income
            incomeViewModel.update(result)
            val index = listIncome.indexOfFirst { it.income_id == result.income_id }

            if (index != -1) {
                // Thay thế phần tử tại chỉ số đó
                listIncome[index] = result
            }
            incomeController.updateItemUpdateList(result)
        }

        incomeViewModel.getIncomeWithUserId(SharedPreferenceUtils.keyUserLogin).observe(this) { list ->
            if (!isLoadData) {
                listIncome.addAll(list)
                isLoadData = true
                incomeController.setDataListItem(listIncome)
                binding.epxListIncome.setControllerAndBuildModels(incomeController)
                incomeController.setOnClick { income ->
                    itemIncome = income
                    addFragment(DetailIncomeFragment.instance(income.income_id))

                }
                incomeController.setOnClickMore { audioHistory, view ->
                    showPopupMenu(view, audioHistory)
                }
                if (listIncome.isEmpty()) {
                    binding.lnEmpty.makeVisible()
                    binding.epxListIncome.makeGone()
                }
            }
        }
    }
    private fun filterWithSearch(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(listIncome)
        } else {
            val lowerCaseQuery = query.lowercase()
            for (item in listIncome) {
                if (item.description.lowercase().contains(lowerCaseQuery)) {
                    filteredList.add(item)
                }
            }
        }
        incomeController.setDataListItem(filteredList)
    }
    @SuppressLint("MissingInflatedId")
    private fun showPopupMenu(view: View, income: Income) {
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
            addFragment(DetailIncomeFragment.instance(income.income_id))
            popupMenu.dismiss()
        }
        layout.findViewById<LinearLayout>(R.id.ll_item_delete).setOnClickListener {
            showDialogConfirmDelete(income)
            popupMenu.dismiss()
        }

        popupMenu.showAsDropDown(view, -200, 0, Gravity.NO_GRAVITY)
    }
    private fun showDialogConfirmDelete(income: Income) {
        val deleteDialog = DeleteDialog(this) {
            incomeViewModel.delete(income)
            listIncome.remove(income)
            if (listIncome.isEmpty()) {
                binding.lnEmpty.makeVisible()
                binding.epxListIncome.makeGone()
            }
            incomeController.deleteAudioAndUpdateList(income)
        }
        if (!deleteDialog.isShowing) {
            deleteDialog.show()
        }
    }
}