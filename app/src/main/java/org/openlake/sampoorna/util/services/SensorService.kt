package org.openlake.sampoorna.util.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.telephony.SmsManager
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.openlake.sampoorna.R
import org.openlake.sampoorna.data.sources.entities.Contact
import org.openlake.sampoorna.presentation.features.contacts.ContactsViewModel
import javax.inject.Inject

class SensorService @Inject constructor(val viewModel: ContactsViewModel) : Service() ,PowerButtonListener.OnDoubleClickListener{
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startNewForegroundService()
        } else {
            startForeground(1, Notification())
        }
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

    override fun onClick(count: Int) {
        TODO("Not yet implemented")
    }

    @SuppressLint("MissingPermission")
    fun sosFeature() {


        val fusedLocationProviderClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(applicationContext)
        val contactsList = viewModel.allContacts.value as ArrayList<Contact>
        val smsManager: SmsManager = SmsManager.getDefault()
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            val lat = it.latitude
            val long = it.longitude
            if (contactsList != null) {
                for (contact in contactsList){
                    val message="oops dummy ${contact.name} $lat $long"
                    smsManager.sendTextMessage(contact.contact,null,message,null,null)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
