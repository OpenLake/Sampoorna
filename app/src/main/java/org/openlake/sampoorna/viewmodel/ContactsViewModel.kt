package org.openlake.sampoorna.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.openlake.sampoorna.models.Contacts
import org.openlake.sampoorna.models.ContactsDatabase
import org.openlake.sampoorna.data.repository.ContactsRepository

class ContactsViewModel(application: Application) :AndroidViewModel(application) {

    val allContacts: LiveData<List<Contacts>>
    private lateinit var repository: ContactsRepository

    init{
        val dao = ContactsDatabase.getDatabase(application).getContactsDao()
        val repository= ContactsRepository(dao)
        allContacts = repository.allContacts
    }

    fun deleteContact(contacts: Contacts)=viewModelScope.launch(Dispatchers.IO) {
        repository.delete(contacts)
    }

    fun insertContact(contact:Contacts)=viewModelScope.launch(Dispatchers.IO){
        repository.insert(contact)
    }


}