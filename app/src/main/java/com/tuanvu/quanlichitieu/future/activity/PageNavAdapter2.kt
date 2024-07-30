package com.tuanvu.quanlichitieu.future.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tuanvu.quanlichitieu.future.fragment.StaticDayFragment
import com.tuanvu.quanlichitieu.future.fragment.StaticDayNotFragment
import com.tuanvu.quanlichitieu.future.fragment.StaticMonthFragment
import com.tuanvu.quanlichitieu.future.fragment.StaticMonthNotFragment
import com.tuanvu.quanlichitieu.future.fragment.StaticYearFragment
import com.tuanvu.quanlichitieu.future.fragment.StaticYearNotFragment

class PageNavAdapter2(owner: FragmentActivity, private val numberPage: Int) :
    FragmentStateAdapter(owner) {
    override fun getItemCount(): Int {
        return numberPage
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StaticDayNotFragment.instance()
            1 -> StaticMonthNotFragment.instance()
            2 -> StaticYearNotFragment.instance()
            else -> {
                StaticDayNotFragment.instance()
            }
        }
    }
}