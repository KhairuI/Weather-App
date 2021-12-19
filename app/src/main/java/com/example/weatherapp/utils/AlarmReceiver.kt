package com.example.weatherapp.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weatherapp.core.AppConstants


class AlarmReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, p1: Intent?) {

        val intent= Intent(AppConstants.INTENT_FILTER)
        intent.putExtra(AppConstants.BROADCAST_KEY, "Alarm Receive")
        LocalBroadcastManager.getInstance(context!!).sendBroadcast(intent)
    }


}