package org.openlake.sampoorna.data.repository

import androidx.lifecycle.LiveData
import org.openlake.sampoorna.data.sources.entities.Contact
import org.openlake.sampoorna.data.sources.entities.ContactEntity

interface ContactsRepository {
     fun fetchAllContacts() :LiveData<List<ContactEntity>>
     suspend fun insert(contact: Contact)
     suspend fun delete(contact: Contact)
}