package com.tuanvu.quanlichitieu.future.epoxy.controller

import android.view.View
import com.airbnb.epoxy.EpoxyController
import com.tuanvu.quanlichitieu.future.database.entity.Income
import com.tuanvu.quanlichitieu.future.database.entity.IncomeCategories
import com.tuanvu.quanlichitieu.itemIncome
import com.tuanvu.quanlichitieu.itemIncomeCategories

class IncomeCategoriesController :
    EpoxyController() {
    private lateinit var callback: (IncomeCategories) -> Unit
    private lateinit var callbackMore: (IncomeCategories, View) -> Unit
    private var listItem = arrayListOf<IncomeCategories>()
    fun setOnClick(callback: (IncomeCategories) -> Unit) {
        this.callback = callback
    }

    fun setOnClickMore(callback: (IncomeCategories, View) -> Unit) {
        this.callbackMore = callback
    }

    fun setDataListItem(listAudio: List<IncomeCategories>) {
        this.listItem.clear()
        listItem.addAll(listAudio)
        requestModelBuild()
    }
    fun updateItemUpdateList(audioHistory: IncomeCategories) {
        val index = listItem.indexOfFirst { it.category_id == audioHistory.category_id }

        if (index != -1) {
            listItem[index] = audioHistory
        }
        requestModelBuild()
    }
    fun deleteAudioAndUpdateList(audioHistory: IncomeCategories) {
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
                    this@IncomeCategoriesController.callback(item)
                }
                onClickMore {view ->
                    this@IncomeCategoriesController.callbackMore(item,view)
                }
            }
        }
    }


}