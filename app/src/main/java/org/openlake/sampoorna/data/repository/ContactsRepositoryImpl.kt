package org.openlake.sampoorna.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import org.openlake.sampoorna.data.di.Transformer
import org.openlake.sampoorna.data.sources.AppDatabase
import org.openlake.sampoorna.data.sources.entities.Contacts
import org.openlake.sampoorna.data.sources.entities.ContactsEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsRepositoryImpl @Inject constructor(val appDatabase: AppDatabase): ContactsRepository{

    override fun fetchAllContacts(): LiveData<List<ContactsEntity>>{
        return appDatabase.contactsDao().getAllContacts()
    }

    override suspend fun insert(contact: Contacts) {
        appDatabase.contactsDao().insert(Transformer.convertContactModelToContactEntity(contact))
    }

    override suspend fun delete(contact: Contacts) {
        appDatabase.contactsDao().delete(Transformer.convertContactModelToContactEntity(contact))
    }
}