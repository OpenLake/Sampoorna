package org.openlake.sampoorna.util.services

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.*

class ScreenReceiver: BroadcastReceiver() {

    private var wasScreenOn = false
    private var count = 0
    private var timebuffer = 0L
    private lateinit var currentTime: Date

    @SuppressLint("NewApi")
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent != null && context != null) {
            if (intent.action == Intent.ACTION_SCREEN_OFF) {
                wasScreenOn = false
                currentTime = Calendar.getInstance().time
                count++
            }
            else if(intent.action == Intent.ACTION_SCREEN_ON){
                val newTime = Calendar.getInstance().time
                val timediff = newTime.time - currentTime.time
                if (timediff<800) {
                    timebuffer += timediff
                }
                else{
                    timebuffer=0
                    count=0
                }
                wasScreenOn = true
                count++
                if (timebuffer<1000){
                    if (count>=4){
                        count=0
                        timebuffer=0
                        SOSService.canSend.postValue(true)
                        return
                    }
                }
                else{
                    count=0
                    timebuffer=0
                }
            }
        }
    }
}
