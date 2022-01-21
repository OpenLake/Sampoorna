package org.openlake.sampoorna.util.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.location.FusedLocationProviderClient
import org.openlake.sampoorna.data.sources.entities.Contact
import org.openlake.sampoorna.presentation.features.userFeatures.UserViewModel

object SOSHelper {
    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.SEND_SMS
    )
    private const val REQUEST_CODE = 101
    var latlong:String = ""
    var locationSettingCheck:Boolean = false
    @SuppressLint("MissingPermission")
    fun getLastLocation(context: Context){
        val locationSetting = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationSetting.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationSettingCheck = true
            val fusedLocationProviderClient = FusedLocationProviderClient(context)
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                val lat = location.latitude.toString()
                val long = location.longitude.toString()
                this.latlong = "\nMy location is:\nhttp://maps.google.com/?q=$lat,$long"
            }
            fusedLocationProviderClient.lastLocation.addOnFailureListener {
                Log.d("Location", "Not Working")
            }
        }
        else{
            Toast.makeText(context, "Please turn on location service", Toast.LENGTH_SHORT).show()
            locationSettingCheck = false
        }
    }

    fun sendSms(listOfContacts:List<Contact>,viewModel:UserViewModel,viewLifecycleOwner: LifecycleOwner,activity: Activity){
        viewModel.userDetails.observe(viewLifecycleOwner,{
            getLastLocation(activity)
            if (!askForPermissions(activity) && locationSettingCheck){
                for (contact in listOfContacts){
                    val smsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage(contact.contact,null,"${it[0].sosMessage} $latlong",null,null)
                    Toast.makeText(activity, "SOS Sent Successfully", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun askForPermissions(activity: Activity): Boolean {
        val permissionsToRequest: MutableList<String> = ArrayList()
        for (permission in PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission)
            }
        }
        if (permissionsToRequest.isEmpty()) {
            return false
        }
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionsToRequest.toTypedArray(), REQUEST_CODE)
        }
        return true
    }
}