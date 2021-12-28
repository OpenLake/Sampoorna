package org.openlake.sampoorna.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.openlake.sampoorna.models.Contacts
import org.openlake.sampoorna.models.ContactsDatabase
import org.openlake.sampoorna.data.repository.ContactsRepository
import org.openlake.sampoorna.data.repository.ContactsRepositoryImpl
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(val repository: ContactsRepositoryImpl, application: Application) : ViewModel() {

     lateinit var allContacts: LiveData<List<Contacts>>

    init{
        val dao = ContactsDatabase.getDatabase(application).getContactsDao()
        val repository= ContactsRepositoryImpl(dao)

    }

    fun deleteContact(contacts: Contacts)=viewModelScope.launch(Dispatchers.IO) {
        repository.delete(contacts)
    }

    fun insertContact(contact:Contacts)=viewModelScope.launch(Dispatchers.IO){
        repository.insert(contact)
    }

    fun fetchC(allContacts: LiveData<List<Contacts>>)=viewModelScope.launch(Dispatchers.IO){
        repository.fetchAllContacts(allContacts)
    }
}