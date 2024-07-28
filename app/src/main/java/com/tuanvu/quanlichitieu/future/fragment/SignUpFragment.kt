package com.tuanvu.quanlichitieu.future.fragment

import android.text.InputType
import android.util.Patterns
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.tuanvu.quanlichitieu.R
import com.tuanvu.quanlichitieu.base.BaseFragment
import com.tuanvu.quanlichitieu.databinding.SignUpFragmentBinding
import com.tuanvu.quanlichitieu.future.application.MyApplication
import com.tuanvu.quanlichitieu.future.database.entity.TableUser
import com.tuanvu.quanlichitieu.future.database.viewmodel.TableUserViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.TableUserViewModelFactory
import com.tuanvu.quanlichitieu.future.ultis.Constants
import com.tuanvu.quanlichitieu.future.ultis.MyListUserLogin
import com.tuanvu.quanlichitieu.future.ultis.makeGone
import com.tuanvu.quanlichitieu.future.ultis.makeVisible


class SignUpFragment : BaseFragment<SignUpFragmentBinding>() {

    companion object {
        fun instance(): SignUpFragment {
            return newInstance(SignUpFragment::class.java)
        }
    }

    private val tableUserViewModel: TableUserViewModel by viewModels {
        TableUserViewModelFactory((requireActivity().application as MyApplication).tableUserRepository)
    }
    private var isShowPass = false
    private var isShowPassEnter = false
    private var isGender = false


    override fun initView() {
        binding.tvSignIn.setOnClickListener {
            replaceFragment(SignInFragment.instance())
        }

        binding.btnSignUp.setOnClickListener {
            if (isValidSignInDetails()) {
                //create auth
                createUser()
            }
        }

        binding.changerGender.setOnClickListener {
            isGender = !isGender
            binding.changerGender.setImageResource(if (isGender) R.drawable.toggle_turn_on else R.drawable.toggle_turn_off)
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

        binding.icEyeEnterPass.setOnClickListener {
            val cursorPosition: Int =
                binding.inputEnterPass.selectionEnd // Lưu vị trí con trỏ hiện tại
            if (!isShowPassEnter) {
                binding.icEyeEnterPass.setImageResource(R.drawable.ic_eye_on)
                binding.inputEnterPass.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            } else {
                binding.icEyeEnterPass.setImageResource(R.drawable.ic_eye_off)
                binding.inputEnterPass.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding.inputEnterPass.setSelection(cursorPosition)
            isShowPassEnter = !isShowPassEnter
        }
    }


    private fun createUser() {
        binding.layoutLoading.makeVisible()
        val nameFull = binding.inputFistName.text.toString().trim()
        val nameUser = binding.inputUserName.text.toString().trim()
        val email = binding.inputEmail.text.toString().trim()
        val pass = binding.inputPass.text.toString().trim()
        val gender = if (isGender) Constants.FEMALE else Constants.MALE
        if (MyListUserLogin.getList().isNotEmpty()){
            MyListUserLogin.getList().forEach {
                if (it.email != email) {
                    val itemUser = TableUser(
                        full_name = nameFull,
                        email = email,
                        username = nameUser,
                        gender = gender,
                        password = pass
                    )
                    tableUserViewModel.insert(itemUser)
                    MyListUserLogin.addItem(itemUser)
                    replaceFragment(SignInFragment.instance())
                    showToast("register success")
                    binding.layoutLoading.makeGone()
                    return
                }
            }
        }
        else{
            val itemUser = TableUser(
                full_name = nameFull,
                email = email,
                username = nameUser,
                gender = gender,
                password = pass
            )
            tableUserViewModel.insert(itemUser)
            MyListUserLogin.addItem(itemUser)
            replaceFragment(SignInFragment.instance())
            showToast("register success")
            binding.layoutLoading.makeGone()
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
        } else if (binding.inputPass.text.toString().trim().isEmpty()) {
            showToast("Valid password")
            false
        } else if (binding.inputPass.text.toString().trim().length < 8) {
            showToast("Password must be 8 characters or more")
            false
        } else if (binding.inputEnterPass.text.toString().trim().length < 8) {
            showToast("Enter Password must be 8 characters or more")
            false
        } else if (binding.inputEnterPass.text.toString().trim().isEmpty()) {
            showToast("Valid Enter password ")
            false
        } else if (binding.inputEnterPass.text.toString()
                .trim() != binding.inputPass.text.toString().trim()
        ) {
            showToast("Confirm the password needs to match your password")
            false
        } else if (binding.inputFistName.text.toString().trim().isEmpty()) {
            showToast("Valid fistName")
            false
        } else {
            true
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): SignUpFragmentBinding {
        return SignUpFragmentBinding.inflate(inflater, container, false)
    }
}