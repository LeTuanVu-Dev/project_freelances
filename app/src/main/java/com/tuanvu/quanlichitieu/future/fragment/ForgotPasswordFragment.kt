package com.tuanvu.quanlichitieu.future.fragment

import android.util.Patterns
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.tuanvu.quanlichitieu.base.BaseFragment
import com.tuanvu.quanlichitieu.databinding.ForgotPasswordFragmentBinding
import com.tuanvu.quanlichitieu.future.application.MyApplication
import com.tuanvu.quanlichitieu.future.database.viewmodel.TableUserViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.TableUserViewModelFactory
import com.tuanvu.quanlichitieu.future.ultis.MyListUserLogin
import com.tuanvu.quanlichitieu.future.ultis.makeGone
import com.tuanvu.quanlichitieu.future.ultis.makeVisible
import papaya.`in`.sendmail.SendMail
import kotlin.random.Random
import kotlin.random.nextInt

class ForgotPasswordFragment : BaseFragment<ForgotPasswordFragmentBinding>() {

    companion object {
        fun instance(): ForgotPasswordFragment {
            return newInstance(ForgotPasswordFragment::class.java)
        }
    }
    private var random: Int = 0
    private val tableUserViewModel: TableUserViewModel by viewModels {
        TableUserViewModelFactory((requireActivity().application as MyApplication).tableUserRepository)
    }
    override fun initView() {
//        showToast("${MyListUserLogin.getList().size}")
        binding.icBack.setOnClickListener {
            handlerBackPressed()
        }
        binding.btnSendEmail.setOnClickListener {
            val email = binding.inputEmail.text.toString().trim()

            val isCheckExist = MyListUserLogin.getList().any { it.email == email }

            if (!isCheckExist){
                showToast("email not register app")
            }
            else if (isValidSignInDetails()) {
                binding.ctnOTP.makeVisible()
                binding.btnSendEmail.makeGone()
                binding.btnCheckOtp.makeVisible()
                random()
            }
        }
        binding.btnCheckOtp.setOnClickListener {
            val email = binding.inputEmail.text.toString().trim()

            val pasNew = binding.inputEnterPass.text.toString().trim()
            val otp = binding.inputOTP.text.trim().toString()

            if (otp.isNotEmpty() && otp == random.toString()){
                binding.ctnPass.makeVisible()
                binding.ctnEnterPass.makeVisible()
                binding.ctnOTP.makeGone()
            }
            if (isValidSignInAccept()) {
                val item = MyListUserLogin.getList().find { it.email == email }
                if (item != null) {
                    item.password = pasNew
                    tableUserViewModel.update(item)
                    // Cập nhật item trong danh sách
                    val userList = MyListUserLogin.getList().toMutableList()
                    val index = userList.indexOfFirst { it.email == item.email }

                    if (index != -1) {
                        userList[index] = item
                        MyListUserLogin.clear()
                        MyListUserLogin.addAll(userList)
                    }
                    handlerBackPressed()
                }
            }

        }
    }


    private fun random() {
        random = Random.nextInt(100000..999999)
        var mail = SendMail(
            "letuanvu425@gmail.com", "ocrpfmykiskydjcc", binding.inputEmail.text.toString().trim(), "Login Signup app's OTP",
            "Your OTP for account authentication is : $random"
        )
        mail.execute()
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
        }
        else {
            true
        }
    }
    private fun isValidSignInAccept(): Boolean {
        // email trống
        return if (binding.inputPass.text.toString().trim().isEmpty()) {
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
        }else {
            true
        }
    }


    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment(this)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ForgotPasswordFragmentBinding {
        return ForgotPasswordFragmentBinding.inflate(inflater, container, false)
    }
}