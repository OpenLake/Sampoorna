package org.openlake.sampoorna.util

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.Transformations
import org.openlake.sampoorna.data.di.Transformer
import org.openlake.sampoorna.data.repository.ContactsRepository

fun Context.toast(message:String){
    Toast.makeText(this, message,Toast.LENGTH_SHORT).show()
}