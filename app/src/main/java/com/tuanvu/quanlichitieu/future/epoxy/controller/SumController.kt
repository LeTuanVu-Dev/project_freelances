package com.tuanvu.quanlichitieu.future.epoxy.controller

import android.util.Log
import com.airbnb.epoxy.EpoxyController
import com.tuanvu.quanlichitieu.R
import com.tuanvu.quanlichitieu.future.application.MyApplication
import com.tuanvu.quanlichitieu.future.ultis.DateAmount
import com.tuanvu.quanlichitieu.itemSum

class SumController :
    EpoxyController() {
    private var listItem = arrayListOf<DateAmount>()
    fun setDataListItem(listAudio: ArrayList<DateAmount>) {
        this.listItem.clear()
        listItem.addAll(listAudio)
        requestModelBuild()
    }

    private fun setTextColor(a: Float, b: Float):Int {
        return if (a - b > 0) MyApplication.instanceApp.getColor(R.color.color_correct) else MyApplication.instanceApp.getColor(R.color.color_incorrect)
    }

    override fun buildModels() {
        Log.d("VuLT", "buildModels: " + listItem.size)
        listItem.forEach { item ->
            itemSum {
                id(item.hashCode())
                valueThu(item.incomeAmount.toString())
                valueChi("- ${item.expenseAmount}")
                sum("${item.incomeAmount - item.expenseAmount}")
                textColor(this@SumController.setTextColor(item.incomeAmount,item.expenseAmount))
                date(item.date)
            }
        }
    }


}