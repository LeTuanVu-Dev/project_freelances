package com.tuanvu.quanlichitieu.future.dialog

import android.content.Context
import com.tuanvu.quanlichitieu.base.BaseDialog
import com.tuanvu.quanlichitieu.databinding.DialogDeleteBinding

class DeleteDialog(
    context: Context
) : BaseDialog(context) {
    private val binding: DialogDeleteBinding =
        DialogDeleteBinding.inflate(layoutInflater)

    constructor(context: Context, callback: () -> Unit) : this(context) {
        this.callback = callback
    }

    private var callback: (() -> Unit)? = null

    init {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        binding.tvHeader.isSelected = true
        binding.tvSubTitle1.isSelected = true
        binding.tvCancel.setOnClickListener {
            dismiss()
        }
        binding.tvDelete.setOnClickListener {
            callback?.invoke()
            dismiss()
        }
    }

}