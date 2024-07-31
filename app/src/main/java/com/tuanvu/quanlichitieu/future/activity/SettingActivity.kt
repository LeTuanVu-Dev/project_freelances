package com.tuanvu.quanlichitieu.future.activity

import android.content.Intent
import com.tuanvu.quanlichitieu.base.BaseActivity
import com.tuanvu.quanlichitieu.databinding.ActivitySettingBinding
import com.tuanvu.quanlichitieu.future.fragment.WelcomeFragment
import com.tuanvu.quanlichitieu.future.preferences.SharedPreferenceUtils

class SettingActivity:BaseActivity<ActivitySettingBinding>() {
    override fun getViewBinding(): ActivitySettingBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun createView() {
        binding.icBack.setOnClickListener {
            finish()
        }
        binding.btnLogout.setOnClickListener {
            signOut()
        }

        binding.btnWelcome.setOnClickListener {
            addFragment(WelcomeFragment.instance())
        }

    }

    private fun signOut() {
        SharedPreferenceUtils.keyUserLogin = -1
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }
}