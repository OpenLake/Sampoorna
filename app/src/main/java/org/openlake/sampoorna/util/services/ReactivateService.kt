package org.openlake.sampoorna.util.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class ReactivateService : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            Log.d("ashu vai","runinnggg")
            context?.startForegroundService(Intent(context,SOSService::class.java))
        }
        else{
            Log.d("ashu vai","runinnggg")
            context?.startService(Intent(context,SOSService::class.java))
        }
    }
}