package org.openlake.sampoorna.data.repository

import androidx.lifecycle.LiveData
import org.openlake.sampoorna.data.di.Transformer
import org.openlake.sampoorna.data.sources.AppDatabase
import org.openlake.sampoorna.models.Contacts
import org.openlake.sampoorna.models.ContactsEntity
import org.openlake.sampoorna.models.ContactsDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsRepositoryImpl @Inject constructor(val appDatabase: AppDatabase): ContactsRepository{

    override fun fetchAllContacts(): LiveData<List<ContactsEntity>> {
        return appDatabase.contactsDao().getAllContacts()
    }

    override fun insert(contact: Contacts) {
        appDatabase.contactsDao().insert(Transformer.convertContactModelToContactEntity(contact))
    }

    override fun delete(contact: Contacts) {
        appDatabase.contactsDao().delete(Transformer.convertContactModelToContactEntity(contact))
    }
}