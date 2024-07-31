package com.tuanvu.quanlichitieu.future.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tuanvu.quanlichitieu.base.BaseFragment
import com.tuanvu.quanlichitieu.databinding.FragmentGioithieuBinding

class WelcomeFragment :BaseFragment<FragmentGioithieuBinding>() {

    companion object{
        fun instance():WelcomeFragment{
            return newInstance(WelcomeFragment::class.java)
        }
    }
    override fun initView() {
        binding.icBack.setOnClickListener {
            handlerBackPressed()
        }
    }

    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment(this)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentGioithieuBinding {
        return FragmentGioithieuBinding.inflate(inflater,container,false)
    }
}