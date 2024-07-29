package com.tuanvu.quanlichitieu.future.epoxy.controller

import android.view.View
import com.airbnb.epoxy.EpoxyController
import com.tuanvu.quanlichitieu.future.database.entity.Income
import com.tuanvu.quanlichitieu.itemIncome

class IncomeController :
    EpoxyController() {
    private lateinit var callback: (Income) -> Unit
    private lateinit var callbackMore: (Income, View) -> Unit
    private var listItem = arrayListOf<Income>()
    fun setOnClick(callback: (Income) -> Unit) {
        this.callback = callback
    }

    fun setOnClickMore(callback: (Income, View) -> Unit) {
        this.callbackMore = callback
    }
    fun setDataItem(listAudio: Income) {
        if (listItem.isEmpty()){
            this.listItem.clear()
        }
        listItem.add(listAudio)
        requestModelBuild()
    }
    fun setDataListItem(listAudio: List<Income>) {
        this.listItem.clear()
        listItem.addAll(listAudio)
        requestModelBuild()
    }
    fun updateItemUpdateList(audioHistory: Income) {
        val index = listItem.indexOfFirst { it.income_id == audioHistory.income_id }
        if (index != -1) {
            listItem[index] = audioHistory
        }
        requestModelBuild()
    }
    fun deleteAudioAndUpdateList(audioHistory: Income) {
        listItem.remove(audioHistory)
        requestModelBuild()
    }

    override fun buildModels() {
        listItem.forEach { item ->
            itemIncome {
                id(item.income_id)
                title(item.description)
                status(item.status)
                date(item.date)
                timeStep(500L)
                onClick {_->
                    this@IncomeController.callback(item)
                }
                onClickMore {view ->
                    this@IncomeController.callbackMore(item,view)
                }
            }
        }
    }


}