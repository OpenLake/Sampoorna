package org.openlake.sampoorna.data.di

import org.openlake.sampoorna.data.sources.entities.Contact
import org.openlake.sampoorna.data.sources.entities.ContactEntity

/**
* This is a transformer class
* We cannot just use our model classes for inserting or getting data from db
* There might be a time when variables might be added or removed in model classes
* So it is always a better practice to have transformer classes
*/
object Transformer {

    fun convertContactModelToContactEntity(contact: Contact): ContactEntity {
        return ContactEntity(
            name = contact.name,
            contact = contact.contact,
            id = contact.id
        )
    }

    fun convertContactEntityToContactModel(contactEntity: ContactEntity): Contact {
        return Contact(
            name = contactEntity.name,
            contact = contactEntity.contact,
            id = contactEntity.id
        )
    }
}