package org.openlake.sampoorna.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import org.openlake.sampoorna.models.Contacts
import org.openlake.sampoorna.models.ContactsDatabase
import org.openlake.sampoorna.models.ContactsRepository

class ContactsViewModel(application: Application) :AndroidViewModel(application) {

    val allContacts: LiveData<List<Contacts>>

    init{
        val dao = ContactsDatabase.getDatabase(application).getContactsDao()
        val repository= ContactsRepository(dao)
        allContacts = repository.allContacts
    }
}