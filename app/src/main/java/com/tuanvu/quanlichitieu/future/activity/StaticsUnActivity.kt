package com.tuanvu.quanlichitieu.future.activity

import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.tuanvu.quanlichitieu.R
import com.tuanvu.quanlichitieu.base.BaseActivity
import com.tuanvu.quanlichitieu.databinding.ActivityStatiticsBinding


class StaticsUnActivity : BaseActivity<ActivityStatiticsBinding>() {
    override fun getViewBinding(): ActivityStatiticsBinding {
        return ActivityStatiticsBinding.inflate(layoutInflater)
    }


    private val DAY_FRAGMENT = 0
    private val MONTH_FRAGMENT = 1
    private val YEAR_FRAGMENT = 2
    private var currentFragment = DAY_FRAGMENT
    override fun createView() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        setUpViewPager()
        binding.tvDay.setOnClickListener { setOnClickItemTab(DAY_FRAGMENT) }
        binding.tvMonth.setOnClickListener { setOnClickItemTab(MONTH_FRAGMENT) }
        binding.tvYear.setOnClickListener { setOnClickItemTab(YEAR_FRAGMENT) }
        binding.frAdd.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setOnClickItemTab(position)
            }
        })
    }

    private fun setTextSelectedMenu(pos: Int) {
        when (pos) {
            0 -> {
                binding.tvDay.setTextColor(ContextCompat.getColor(this, R.color.color_incorrect))
                binding.tvMonth.setTextColor(ContextCompat.getColor(this, R.color.clr_808191))
                binding.tvYear.setTextColor(ContextCompat.getColor(this, R.color.clr_808191))
                if (currentFragment != DAY_FRAGMENT) {
                    currentFragment = DAY_FRAGMENT
                    binding.frAdd.currentItem = currentFragment
                }
            }

            1 -> {
                binding.tvDay.setTextColor(ContextCompat.getColor(this, R.color.clr_808191))
                binding.tvMonth.setTextColor(ContextCompat.getColor(this, R.color.color_incorrect))
                binding.tvYear.setTextColor(ContextCompat.getColor(this, R.color.clr_808191))
                if (currentFragment != MONTH_FRAGMENT) {
                    currentFragment = MONTH_FRAGMENT
                    binding.frAdd.currentItem = currentFragment
                }
            }

            2 -> {
                binding.tvDay.setTextColor(ContextCompat.getColor(this, R.color.clr_808191))
                binding.tvMonth.setTextColor(ContextCompat.getColor(this, R.color.clr_808191))
                binding.tvYear.setTextColor(ContextCompat.getColor(this, R.color.color_incorrect))
                if (currentFragment != YEAR_FRAGMENT) {
                    currentFragment = YEAR_FRAGMENT
                    binding.frAdd.currentItem = currentFragment
                }
            }
        }
    }

    private fun setOnClickItemTab(pos: Int) {
        if (currentFragment != pos) {
            currentFragment = pos
            setTextSelectedMenu(currentFragment)
            binding.frAdd.currentItem = currentFragment
        }
    }

    private fun setUpViewPager() {
        val pageNavAdapter = PageNavAdapter2(this, 3)
        binding.frAdd.adapter = pageNavAdapter
        binding.frAdd.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
        binding.frAdd.setCurrentItem(currentFragment, false)
        binding.frAdd.isUserInputEnabled = false
    }
}