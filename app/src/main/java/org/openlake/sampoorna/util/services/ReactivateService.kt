package org.openlake.sampoorna.util.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import org.openlake.sampoorna.presentation.features.sos_alert.AlertFragment

class ReactivateService: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("Check: ","Receiver Started")
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            context?.startForegroundService(Intent(context,AlertFragment::class.java))
        }
        else{
            context?.startService(Intent(context,AlertFragment::class.java))
        }
    }
}