package org.openlake.sampoorna.util.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import org.openlake.sampoorna.App
import org.openlake.sampoorna.R

public class SensorService: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()

        //Starting the foreground service
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            startNewForegroundService()
        }
        else{
            startForeground(1, Notification())
        }
    //Init PowerButtonListener
        val clickListener = PowerButtonListener()


        }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startNewForegroundService() {
        val notificationChannelId="SOS Service"
        val channelName="Background Service"
        val channel:NotificationChannel=
            NotificationChannel(notificationChannelId,channelName,NotificationManager.IMPORTANCE_MIN)
        val manager= getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
        val notificationBuilder:NotificationCompat.Builder=NotificationCompat.Builder(this,notificationChannelId)
        val notification:Notification=notificationBuilder.setOngoing(true)
            .setContentTitle("You are protected")
            .setContentText("We are there for you")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2,notification)

    }

}
