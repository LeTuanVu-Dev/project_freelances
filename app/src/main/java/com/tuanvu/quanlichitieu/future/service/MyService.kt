package com.tuanvu.quanlichitieu.future.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.tuanvu.quanlichitieu.future.application.MyApplication
import com.tuanvu.quanlichitieu.future.database.viewmodel.ExpenseViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeViewModelFactory
import com.tuanvu.quanlichitieu.future.preferences.SharedPreferenceUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import papaya.`in`.sendmail.SendMail

class MyService : Service() {

    private lateinit var incomeViewModel: IncomeViewModel
    private lateinit var expenseViewModel: ExpenseViewModel
    private val serviceScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        val repository = (application as MyApplication).incomeRepository
        val repository2 = (application as MyApplication).expenseRepository
        incomeViewModel = IncomeViewModel(repository)
        expenseViewModel = ExpenseViewModel(repository2)
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceScope.launch {
            // Hiển thị Toast
            incomeViewModel.getIncomeWithUserIdService(SharedPreferenceUtils.keyUserLogin).collect{
                listIncome ->
                Log.d("VuLT", "onStartCommand: $listIncome")

            }
            expenseViewModel.getExpenseWithUserIdService(SharedPreferenceUtils.keyUserLogin).collect{
                    listExpense ->
                Log.d("VuLT", "onStartCommand: $listExpense")

            }
        }

        Toast.makeText(this, "Alarm Received", Toast.LENGTH_SHORT).show()

        // Dừng dịch vụ sau khi hoàn thành công việc
        stopSelf()

        return START_NOT_STICKY
    }
    private fun setDataSendMail(){
        val mail = SendMail(
            "letuanvu425@gmail.com", "ocrpfmykiskydjcc",
            SharedPreferenceUtils.keyEmailLogin, "Login Signup app's OTP",
            ""
        )
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
