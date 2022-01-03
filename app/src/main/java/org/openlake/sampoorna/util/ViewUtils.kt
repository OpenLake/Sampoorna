package org.openlake.sampoorna.util

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.Transformations
import org.openlake.sampoorna.data.di.Transformer
import org.openlake.sampoorna.data.repository.ContactsRepository

fun Context.toast(message:String){
    Toast.makeText(this, message,Toast.LENGTH_SHORT).show()
}
fun  Context.getListFromLiveData(repository: ContactsRepository){
    var allContacts= Transformations.map(repository.fetchAllContacts()) { list ->

        val temp = list.map {
            Transformer.convertContactEntityToContactModel(it)
        }
        if (temp.isNullOrEmpty()) {
            Resource.failure<String>()
        } else {
            temp
        }
    }
}