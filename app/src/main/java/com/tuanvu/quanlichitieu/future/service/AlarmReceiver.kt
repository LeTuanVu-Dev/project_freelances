package com.tuanvu.quanlichitieu.future.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.media.RingtoneManager
import android.os.Build
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat
import com.tuanvu.quanlichitieu.R
import com.tuanvu.quanlichitieu.future.activity.MainActivity
import com.tuanvu.quanlichitieu.future.application.MyApplication
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeViewModel
import com.tuanvu.quanlichitieu.future.database.viewmodel.IncomeViewModelFactory
import com.tuanvu.quanlichitieu.future.preferences.SharedPreferenceUtils
import com.tuanvu.quanlichitieu.future.ultis.AppExtensions.setDailyAlarm
import papaya.`in`.sendmail.SendMail

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Tạo kênh thông báo cho Android O và mới hơn
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("alarm_channel", "Alarm Notifications", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val notification = NotificationCompat.Builder(context, "alarm_channel")
            .setContentTitle("Alarm")
            .setContentText("Your alarm is ringing")
            .setSmallIcon(R.mipmap.ic_launcher) // Bạn cần có biểu tượng thích hợp trong drawable
            .setContentIntent(pendingIntent)
            .setSound(alarmSound)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(0, notification)
        setDailyAlarm(context)
        val serviceIntent = Intent(context, MyService::class.java)
        context.startService(serviceIntent)
    }

}
