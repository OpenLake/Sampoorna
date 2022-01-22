package org.openlake.sampoorna.util.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import org.openlake.sampoorna.presentation.MainActivity

class ReactivateService : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (MainActivity.SOSSwitch.value==true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context?.startForegroundService(Intent(context, SOSService::class.java))
            } else {
                context?.startService(Intent(context, SOSService::class.java))
            }
        }
        else{
            context?.stopService(Intent(context,SOSService::class.java))
        }
    }
}
