package org.openlake.sampoorna.data.repository

import androidx.lifecycle.LiveData
import org.openlake.sampoorna.data.sources.entities.Contacts
import org.openlake.sampoorna.data.sources.entities.ContactsEntity

interface ContactsRepository {
     fun fetchAllContacts() : LiveData<List<ContactsEntity>>
     suspend fun insert(contact: Contacts)
     suspend fun delete(contact: Contacts)
}