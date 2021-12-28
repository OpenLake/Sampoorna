package org.openlake.sampoorna.data.di

import org.openlake.sampoorna.models.Contacts
import org.openlake.sampoorna.models.ContactsEntity

/*
* This is a transformer class
* We cannot just use our model classes for inserting or getting data from db
* There might be a time when variables might be added or removed in model classes
* So it is always a better practice to have transformer classes
* */
object Transformer {

    fun convertContactModelToContactEntity(contact: Contacts): ContactsEntity {
        return ContactsEntity(
            name = contact.name,
            contact = contact.contact
        )
    }

    fun convertContactEntityToContactModel(contactEntity: ContactsEntity): Contacts {
        return Contacts(
            name = contactEntity.name,
            contact = contactEntity.contact
        )
    }
}