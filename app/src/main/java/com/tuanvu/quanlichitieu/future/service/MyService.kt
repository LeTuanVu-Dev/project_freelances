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
import com.tuanvu.quanlichitieu.future.database.entity.Income
import com.tuanvu.quanlichitieu.future.database.entity.TableExpense
import com.tuanvu.quanlichitieu.future.database.viewmodel.ExpenseViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeViewModelFactory
import com.tuanvu.quanlichitieu.future.preferences.SharedPreferenceUtils
import com.tuanvu.quanlichitieu.future.ultis.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        val incomeChannel = Channel<List<Income>>(Channel.UNLIMITED)
        val expenseChannel = Channel<List<TableExpense>>(Channel.UNLIMITED)

        serviceScope.launch {
            // Hiển thị Toast
            launch {
                incomeViewModel.getIncomeWithUserIdService(SharedPreferenceUtils.keyUserLogin).collect { listIncome ->
                    incomeChannel.send(listIncome)
                }
            }

           launch {
                expenseViewModel.getExpenseWithUserIdService(SharedPreferenceUtils.keyUserLogin).collect { listExpense ->
                    expenseChannel.send(listExpense)
                }
            }

            // Lấy dữ liệu từ channel
            val listIncome = incomeChannel.receive()
            val listExpense = expenseChannel.receive()

            // Thực hiện các thao tác với listIncome và listExpense
            // Ví dụ: Hiển thị các khoản chưa thu và chưa chi
            val incomeNames = listIncome.filter { it.status == Constants.NOT_RECEIVED }.map { it.description }
            val expenseNames = listExpense.filter { it.status == Constants.UNPAID }.map { it.description }

            val message = "Bạn hãy chú ý các khoản thu chi chưa được thực hiện: \n" +
                    "khoản chưa thu: $incomeNames, \n"+
                    "khoản chưa chi: $expenseNames"
            Log.d("Cuong", "onStartCommand: message = $message")
            setDataSendMail(message)
            withContext(Dispatchers.Main){

                Toast.makeText(this@MyService, "Alarm Received", Toast.LENGTH_SHORT).show()

                // Dừng dịch vụ sau khi hoàn thành công việc
                stopSelf()
            }
        }


        return START_NOT_STICKY
    }
    private fun setDataSendMail(message:String){
        val mail = SendMail(
            "letuanvu425@gmail.com", "ocrpfmykiskydjcc",
            SharedPreferenceUtils.keyEmailLogin, "Cảnh báo thu chi cá nhân",
            message
        )
        mail.execute()
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
