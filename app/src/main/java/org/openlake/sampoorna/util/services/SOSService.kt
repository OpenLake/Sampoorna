package org.openlake.sampoorna.util.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Build
import android.telephony.SmsManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import org.openlake.sampoorna.R
import org.openlake.sampoorna.data.constants.Constants
import org.openlake.sampoorna.presentation.MainActivity

class SOSService : LifecycleService() {

    private var latlong: String = ""
    private var sosMessage: String? = ""
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var contactsListPreferences: SharedPreferences
    private lateinit var sosMessagePreferences: SharedPreferences
    private var screenReceiver: ScreenReceiver? = null
    private val TAG = "SOSService"

    companion object {
        var canSend = MutableLiveData<Boolean>()
    }

    @SuppressLint("MissingPermission", "VisibleForTests")
    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startSOSForeground()
        }

        canSend.postValue(false)

        contactsListPreferences = this.getSharedPreferences("sosContacts", Context.MODE_PRIVATE)
        sosMessagePreferences = this.getSharedPreferences(Constants.Sampoorna, Context.MODE_PRIVATE)

        sosMessage = sosMessagePreferences.getString(Constants.SOSMessage, "Hello, I am in danger")

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        canSend.observe(this) { send ->
            if (send) {
                val locationRequest = CurrentLocationRequest.Builder()
                    .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    .setMaxUpdateAgeMillis(0)
                    .build()

                fusedLocationProviderClient.getCurrentLocation(locationRequest, null)
                    .addOnSuccessListener { location ->
                        latlong = if (location != null) {
                            val lat = location.latitude
                            val long = location.longitude
                            "\nMy location is:\nhttp://maps.google.com/?q=$lat,$long"
                        } else {
                            "\nLocation unavailable"
                        }
                        sendSmsToContacts()
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Failed to get location", e)
                        latlong = "\nLocation unavailable"
                        sendSmsToContacts()
                    }
            }
        }
    }

    private fun sendSmsToContacts() {
        try {
            val contactSet: MutableSet<String>? =
                contactsListPreferences.getStringSet("contacts", mutableSetOf())
            if (contactSet.isNullOrEmpty()) {
                Log.w(TAG, "No SOS contacts found")
                return
            }

            val smsManager = SmsManager.getDefault()
            for (contact in contactSet) {
                try {
                    smsManager.sendTextMessage(
                        contact,
                        null,
                        "$sosMessage\n$latlong",
                        null,
                        null
                    )
                    Log.d(TAG, "SOS message sent to $contact")
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to send SOS message to $contact", e)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending SOS messages", e)
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        // Only register if not already registered
        if (screenReceiver == null) {
            try {
                val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
                filter.addAction(Intent.ACTION_SCREEN_OFF)
                screenReceiver = ScreenReceiver()
                registerReceiver(screenReceiver, filter)
                Log.d(TAG, "ScreenReceiver registered successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to register ScreenReceiver", e)
            }
        }

        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun startSOSForeground() {
        val notifChannelId = getString(R.string.app_name)
        val channelName = "Background Service"
        val channel = NotificationChannel(
            notifChannelId, channelName, NotificationManager.IMPORTANCE_MIN
        )
        val manager = getSystemService(Context.NOTIFICATION_SERVICE)!! as NotificationManager
        manager.createNotificationChannel(channel)
        val notificationBuild = NotificationCompat.Builder(this, notifChannelId)
        val notification = notificationBuild.setOngoing(true)
            .setContentTitle(getString(R.string.you_are_protected))
            .setSmallIcon(R.mipmap.ic_womenlogo_round)
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)
    }

    override fun onDestroy() {
        // Unregister the receiver before calling super.onDestroy()
        try {
            if (screenReceiver != null) {
                unregisterReceiver(screenReceiver)
                screenReceiver = null
                Log.d(TAG, "ScreenReceiver unregistered successfully")
            }
        } catch (e: IllegalArgumentException) {
            Log.w(TAG, "ScreenReceiver was not registered or already unregistered", e)
        } catch (e: Exception) {
            Log.e(TAG, "Error unregistering ScreenReceiver", e)
        }

        // Then continue with existing onDestroy code
        if (MainActivity.SOSSwitch.value == true) {
            try {
                val broadcastIntent = Intent()
                broadcastIntent.action = "restart Service"
                broadcastIntent.setClass(this, ReactivateService::class.java)
                this.sendBroadcast(broadcastIntent)
                Log.d(TAG, "Sent broadcast to restart service")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to send restart broadcast", e)
            }
        }

        super.onDestroy()
    }
}