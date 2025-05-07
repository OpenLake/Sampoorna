package org.openlake.sampoorna.data.di

import android.os.Build
import androidx.annotation.RequiresApi
import org.openlake.sampoorna.data.repository.CryptoManager
import org.openlake.sampoorna.data.sources.entities.Contact
import org.openlake.sampoorna.data.sources.entities.ContactEntity

/**
* This is a transformer class
* We cannot just use our model classes for inserting or getting data from db
* There might be a time when variables might be added or removed in model classes
* So it is always a better practice to have transformer classes
*/
object Transformer {

    @RequiresApi(Build.VERSION_CODES.M)
    fun convertContactModelToContactEntity(contact: Contact): ContactEntity {
        return ContactEntity(
            id = contact.id,
            name = contact.name?.let { CryptoManager.encrypt(it) },
            contact = contact.contact?.let { CryptoManager.encrypt(it) }
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun convertContactEntityToContactModel(entity: ContactEntity): Contact {
        return Contact(
            id = entity.id,
            name = entity.name?.let { CryptoManager.decrypt(it) },
            contact = entity.contact?.let { CryptoManager.decrypt(it) }
        )
    }
}
