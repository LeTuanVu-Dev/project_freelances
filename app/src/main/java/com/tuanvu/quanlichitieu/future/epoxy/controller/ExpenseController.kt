package com.tuanvu.quanlichitieu.future.epoxy.controller

import android.view.View
import com.airbnb.epoxy.EpoxyController
import com.tuanvu.quanlichitieu.future.database.entity.Income
import com.tuanvu.quanlichitieu.future.database.entity.TableExpense
import com.tuanvu.quanlichitieu.itemExpense
import com.tuanvu.quanlichitieu.itemIncome

class ExpenseController :
    EpoxyController() {
    private lateinit var callback: (TableExpense) -> Unit
    private lateinit var callbackMore: (TableExpense, View) -> Unit
    private var listItem = arrayListOf<TableExpense>()
    fun setOnClick(callback: (TableExpense) -> Unit) {
        this.callback = callback
    }

    fun setOnClickMore(callback: (TableExpense, View) -> Unit) {
        this.callbackMore = callback
    }

    fun setDataListItem(listAudio: List<TableExpense>) {
        this.listItem.clear()
        listItem.addAll(listAudio)
        requestModelBuild()
    }
    fun updateItemUpdateList(audioHistory: TableExpense) {
        val index = listItem.indexOfFirst { it.expense_id == audioHistory.expense_id }

        if (index != -1) {
            listItem[index] = audioHistory
        }
        requestModelBuild()
    }
    fun deleteAudioAndUpdateList(audioHistory: TableExpense) {
        listItem.remove(audioHistory)
        requestModelBuild()
    }

    override fun buildModels() {
        listItem.forEach { item ->
            itemExpense {
                id(item.expense_id)
                title(item.description)
                status(item.status)
                date(item.date)
                timeStep(500L)
                onClick {_->
                    this@ExpenseController.callback(item)
                }
                onClickMore {view ->
                    this@ExpenseController.callbackMore(item,view)
                }
            }
        }
    }


}