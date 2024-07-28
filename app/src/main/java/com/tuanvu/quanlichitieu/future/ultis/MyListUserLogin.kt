package com.tuanvu.quanlichitieu.future.ultis

import com.tuanvu.quanlichitieu.future.database.entity.TableUser

object MyListUserLogin {

    private val mList: MutableList<TableUser> = mutableListOf()

    fun getList(): List<TableUser> {
        return mList
    }

    fun clear() {
        mList.clear()
    }
    fun addItem(data: TableUser) {
        mList.add(data)
    }
    fun addAll(data: List<TableUser>) {
        mList.clear()
        mList.addAll(data)
    }
}