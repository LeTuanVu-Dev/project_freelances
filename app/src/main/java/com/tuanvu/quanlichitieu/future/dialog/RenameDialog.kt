package com.tuanvu.quanlichitieu.future.dialog

import android.content.Context
import com.tuanvu.quanlichitieu.base.BaseDialog
import com.tuanvu.quanlichitieu.databinding.DialogRenameBinding

class RenameDialog(
    context: Context
) : BaseDialog(context) {
    private val binding: DialogRenameBinding =
        DialogRenameBinding.inflate(layoutInflater)

    constructor(context: Context, callback: (String) -> Unit) : this(context) {
        this.callback = callback
    }

    private var callback: ((String) -> Unit)? = null

    init {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setContentView(binding.root)
        setUpView()
    }

    fun setTextCurrentName(name: String) {
        binding.edInput.setText(name)
    }

    fun getTextCurrentName(): String {
        return binding.edInput.text.toString().trim()
    }

    private fun setUpView() {
        binding.tvHeader.isSelected = true
        binding.ivClose.setOnClickListener {
            binding.edInput.setText("")
        }
        binding.tvCancel.setOnClickListener {
            dismiss()
        }
        binding.tvSave.setOnClickListener {
            callback?.invoke(getTextCurrentName())
            dismiss()
        }
    }

}