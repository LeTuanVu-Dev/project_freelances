package com.tuanvu.quanlichitieu.future.activity

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.tuanvu.quanlichitieu.base.BaseActivity
import com.tuanvu.quanlichitieu.databinding.ActivitySignInUpBinding
import com.tuanvu.quanlichitieu.future.application.MyApplication
import com.tuanvu.quanlichitieu.future.database.viewmodel.TableUserViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.TableUserViewModelFactory
import com.tuanvu.quanlichitieu.future.fragment.SignInFragment
import com.tuanvu.quanlichitieu.future.preferences.SharedPreferenceUtils
import com.tuanvu.quanlichitieu.future.ultis.MyListUserLogin

class LoginActivity : BaseActivity<ActivitySignInUpBinding>() {
    override fun getViewBinding(): ActivitySignInUpBinding {
        return ActivitySignInUpBinding.inflate(layoutInflater)
    }

    private val tableUserViewModel: TableUserViewModel by viewModels {
        TableUserViewModelFactory((application as MyApplication).tableUserRepository)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun createView() {
        Log.d("VuLT", "createView: LoginActivity"+SharedPreferenceUtils.keyUserLogin)
        if (SharedPreferenceUtils.keyUserLogin < 0) {
            if (MyListUserLogin.getList().isEmpty()) {
                tableUserViewModel.allTableUser.observe(this) {
                    if (it.isNotEmpty()) {
                        MyListUserLogin.addAll(it)
                    }
                    replaceFragment(SignInFragment.instance())
                }
            } else {
                replaceFragment(SignInFragment.instance())
            }
        } else {
            Log.d("VuLT", "createView: LoginActivity startActivity")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}