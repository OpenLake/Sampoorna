package org.openlake.sampoorna.util.services

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.openlake.sampoorna.R
import org.openlake.sampoorna.data.constants.Constants
import org.openlake.sampoorna.presentation.MainActivity

class PeriodReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val sharedPreferences = context.getSharedPreferences(Constants.Sampoorna, Context.MODE_PRIVATE)

        val resultIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        resultIntent.putExtra("fragment", R.id.trackingFragment)

        val pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT else PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, Constants.NotificationChannel)
            .setSmallIcon(R.drawable.periods)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.periods))
            .setContentTitle(intent.getStringExtra("title"))
            .setContentText(intent.getStringExtra("content"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(1, notification)
            sharedPreferences.edit()
                .remove("expectedDate")
                .apply()
        }

    }

}