package com.tuanvu.quanlichitieu.future.activity

import com.tuanvu.quanlichitieu.base.BaseActivity
import com.tuanvu.quanlichitieu.databinding.ActivityStatiticsBinding


class StaticsActivity : BaseActivity<ActivityStatiticsBinding>() {
    override fun getViewBinding(): ActivityStatiticsBinding {
        return ActivityStatiticsBinding.inflate(layoutInflater)
    }

    override fun createView() {

    }
}