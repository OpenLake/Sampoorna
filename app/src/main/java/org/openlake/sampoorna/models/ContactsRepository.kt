package org.openlake.sampoorna.models

import androidx.lifecycle.LiveData

class ContactsRepository(private val contactsDao: ContactsDao) {
    val allContacts: LiveData<List<Contacts>> = contactsDao.getAllContacts()
    suspend fun insert(contact:Contacts){
        contactsDao.insert(contact)
    }
    suspend fun delete(contact: Contacts){
        contactsDao.delete(contact)
    }
}