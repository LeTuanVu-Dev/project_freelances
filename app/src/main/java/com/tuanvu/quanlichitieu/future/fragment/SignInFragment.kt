package com.tuanvu.quanlichitieu.future.fragment

import android.content.Intent
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.tuanvu.quanlichitieu.R
import com.tuanvu.quanlichitieu.base.BaseFragment
import com.tuanvu.quanlichitieu.databinding.LoginFragmentBinding
import com.tuanvu.quanlichitieu.future.activity.MainActivity
import com.tuanvu.quanlichitieu.future.preferences.SharedPreferenceUtils
import com.tuanvu.quanlichitieu.future.ultis.MyListUserLogin


class SignInFragment : BaseFragment<LoginFragmentBinding>() {

    companion object {
        fun instance(): SignInFragment {
            return newInstance(SignInFragment::class.java)
        }
    }


    private var isShowPass = false
    override fun initView() {
        Log.d("VuLT", "createView: SignInFragment")

        binding.tvSignUp.setOnClickListener {
            replaceFragment(SignUpFragment.instance())
        }

        binding.tvForgotPass.setOnClickListener {
            addFragment(ForgotPasswordFragment.instance())
        }

        binding.icEyePass.setOnClickListener {
            val cursorPosition: Int = binding.inputPass.selectionEnd // Lưu vị trí con trỏ hiện tại
            if (!isShowPass) {
                binding.icEyePass.setImageResource(R.drawable.ic_eye_on)
                binding.inputPass.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            } else {
                binding.icEyePass.setImageResource(R.drawable.ic_eye_off)
                binding.inputPass.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding.inputPass.setSelection(cursorPosition)
            isShowPass = !isShowPass
        }

        binding.btnSignIn.setOnClickListener {
            if (isValidSignInDetails()) {
                signIn()
            }
        }
    }

    private fun signIn() {
        val userName = binding.inputEmail.text.toString().trim()
        val pass = binding.inputPass.text.toString().trim()

        val isCheckExist = MyListUserLogin.getList().any { it.email == userName && it.password == pass }
        val item = MyListUserLogin.getList().find { it.email == userName && it.password == pass }

        if (isCheckExist && item != null) {
            // Thực hiện hành động nếu item tồn tại
            SharedPreferenceUtils.keyUserLogin = item.user_id
            SharedPreferenceUtils.keyUserNameLogin = item.full_name
            startActivity(Intent(requireContext(),MainActivity::class.java))
            requireActivity().finish()
        } else {
            // Thực hiện hành động nếu item không tồn tại
            showToast("wrong login information")
        }


    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun isValidSignInDetails(): Boolean {
        // email trống
        return if (binding.inputEmail.text.toString().trim().isEmpty()) {
            showToast("Enter email")
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.text.toString().trim())
                .matches()
        ) {
            showToast("Enter valid email")
            false
        } else if (binding.inputPass.text.toString().trim().length < 8) {
            showToast("Password must >= 8 size")
            false
        } else if (binding.inputPass.text.toString().trim().isEmpty()) {
            showToast("Enter password")
            false
        } else {
            true
        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): LoginFragmentBinding {
        return LoginFragmentBinding.inflate(inflater, container, false)
    }
}