package org.openlake.sampoorna.data.repository

import androidx.lifecycle.LiveData
import org.openlake.sampoorna.models.Contacts
import org.openlake.sampoorna.models.ContactsDao

interface ContactsRepository {

    suspend fun fetchAllContacts(contactsList: LiveData<List<Contacts>>) :LiveData<List<Contacts>>

    suspend fun insert(contact: Contacts)

    suspend fun delete(contact: Contacts)
}