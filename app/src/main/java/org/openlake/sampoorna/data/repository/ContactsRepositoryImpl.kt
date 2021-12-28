package org.openlake.sampoorna.data.repository

import androidx.lifecycle.LiveData
import org.openlake.sampoorna.models.Contacts
import org.openlake.sampoorna.models.ContactsDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsRepositoryImpl @Inject constructor(val contactsDao: ContactsDao): ContactsRepository{


    override suspend fun fetchAllContacts(contactsList: LiveData<List<Contacts>>): LiveData<List<Contacts>> {
     return contactsDao.getAllContacts()
    }

    override suspend fun insert(contact: Contacts) {
    contactsDao.insert(contact)
    }

    override suspend fun delete(contact: Contacts) {
    contactsDao.delete(contact)
    }

}