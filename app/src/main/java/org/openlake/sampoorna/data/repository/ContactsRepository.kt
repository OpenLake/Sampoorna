package org.openlake.sampoorna.data.repository

import androidx.lifecycle.LiveData
import org.openlake.sampoorna.models.Contacts
import org.openlake.sampoorna.models.ContactsEntity

interface ContactsRepository {
    fun fetchAllContacts() : LiveData<List<ContactsEntity>>
    fun insert(contact: Contacts)
    fun delete(contact: Contacts)
}