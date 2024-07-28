package com.tuanvu.quanlichitieu.future.epoxy.controller

import android.view.View
import com.airbnb.epoxy.EpoxyController
import com.tuanvu.quanlichitieu.future.database.entity.ExpenseCategories
import com.tuanvu.quanlichitieu.future.database.entity.Income
import com.tuanvu.quanlichitieu.future.database.entity.IncomeCategories
import com.tuanvu.quanlichitieu.itemIncome
import com.tuanvu.quanlichitieu.itemIncomeCategories

class ExpenseCategoriesController :
    EpoxyController() {
    private lateinit var callback: (ExpenseCategories) -> Unit
    private lateinit var callbackMore: (ExpenseCategories, View) -> Unit
    private var listItem = arrayListOf<ExpenseCategories>()
    fun setOnClick(callback: (ExpenseCategories) -> Unit) {
        this.callback = callback
    }

    fun setOnClickMore(callback: (ExpenseCategories, View) -> Unit) {
        this.callbackMore = callback
    }

    fun setDataListItem(listAudio: List<ExpenseCategories>) {
        this.listItem.clear()
        listItem.addAll(listAudio)
        requestModelBuild()
    }
    fun updateItemUpdateList(audioHistory: ExpenseCategories) {
        val index = listItem.indexOfFirst { it.category_id == audioHistory.category_id }

        if (index != -1) {
            listItem[index] = audioHistory
        }
        requestModelBuild()
    }
    fun deleteAudioAndUpdateList(audioHistory: ExpenseCategories) {
        listItem.remove(audioHistory)
        requestModelBuild()
    }

    override fun buildModels() {
        listItem.forEach { item ->
            itemIncomeCategories {
                id(item.category_id)
                title(item.name)
                timeStep(500L)
                onClick {_->
                    this@ExpenseCategoriesController.callback(item)
                }
                onClickMore {view ->
                    this@ExpenseCategoriesController.callbackMore(item,view)
                }
            }
        }
    }


}