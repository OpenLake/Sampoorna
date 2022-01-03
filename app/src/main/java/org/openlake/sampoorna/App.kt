package org.openlake.sampoorna

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.openlake.sampoorna.data.sources.entities.Contacts

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}