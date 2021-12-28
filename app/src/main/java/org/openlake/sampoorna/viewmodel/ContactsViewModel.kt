package org.openlake.sampoorna.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import org.openlake.sampoorna.data.di.Transformer
import org.openlake.sampoorna.data.repository.ContactsRepository
import org.openlake.sampoorna.models.Contacts
import org.openlake.sampoorna.models.ContactsEntity
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

    fun deleteContact(contacts: Contacts) {
        repository.delete(contacts)
    }

    fun insertContact(contact:Contacts) {
        repository.insert(contact)
    }
}