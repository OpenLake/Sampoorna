package org.openlake.sampoorna.data.repository

import androidx.lifecycle.LiveData
import org.openlake.sampoorna.data.di.Transformer
import org.openlake.sampoorna.data.sources.ContactDatabase
import org.openlake.sampoorna.data.sources.entities.Contact
import org.openlake.sampoorna.data.sources.entities.ContactEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsRepositoryImpl @Inject constructor(private val appDatabase: ContactDatabase): ContactsRepository {

    override fun fetchAllContacts(): LiveData<List<ContactEntity>> {
        return appDatabase.contactsDao().getAllContacts()
    }

    override suspend fun insert(contact: Contact) {
        appDatabase.contactsDao().insert(Transformer.convertContactModelToContactEntity(contact))
    }

    override suspend fun delete(contact: Contact) {
        appDatabase.contactsDao().delete(Transformer.convertContactModelToContactEntity(contact))
    }
}