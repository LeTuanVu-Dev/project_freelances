package com.tuanvu.quanlichitieu.future.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tuanvu.quanlichitieu.future.fragment.StaticDayFragment
import com.tuanvu.quanlichitieu.future.fragment.StaticMonthFragment
import com.tuanvu.quanlichitieu.future.fragment.StaticYearFragment

class PageNavAdapter(owner: FragmentActivity, private val numberPage: Int) :
    FragmentStateAdapter(owner) {
    override fun getItemCount(): Int {
        return numberPage
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StaticDayFragment.instance()
            1 -> StaticMonthFragment.instance()
            2 -> StaticYearFragment.instance()
            else -> {
                StaticDayFragment.instance()
            }
        }
    }
}