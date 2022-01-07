package org.openlake.sampoorna.presentation.features.contacts

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.openlake.sampoorna.data.di.Transformer
import org.openlake.sampoorna.data.repository.ContactsRepository
import org.openlake.sampoorna.data.sources.entities.Contacts
import org.openlake.sampoorna.util.Resource
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(val repository: ContactsRepository) : ViewModel() {
    var allContacts = Transformations.map(repository.fetchAllContacts()) { list ->

        val temp = list.map {
            Transformer.convertContactEntityToContactModel(it)
        }
        if (temp.isNullOrEmpty()) {
            Resource.failure<String>()
        } else {
            temp
        }
    }
    fun deleteContact(contact: Contacts)=viewModelScope.launch(Dispatchers.IO) {
        repository.delete(contact)
        Log.d("deleteVM Coroutine","${contact}")
    }

    fun insertContact(contact: Contacts)=viewModelScope.launch(Dispatchers.IO) {
        repository.insert(contact)
    }
}