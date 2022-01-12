package org.openlake.sampoorna.util.services

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient

object FetchLocation {
    var latlong:String = ""
    @SuppressLint("MissingPermission")
    fun getLastLocation(context: Context ):String{
        var lat = ""
        var long = ""
        val fusedLocationProviderClient = FusedLocationProviderClient(context)
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location->
             lat = location.latitude.toString()
             long = location.longitude.toString()
            this.latlong = "$lat $long"
            Log.d("Object wala latlong","$lat $long")


        }
        fusedLocationProviderClient.lastLocation.addOnFailureListener {
            Log.d("latlong","no")
        }
        return latlong


    }
}