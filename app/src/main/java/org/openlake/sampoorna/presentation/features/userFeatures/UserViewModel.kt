package org.openlake.sampoorna.presentation.features.userFeatures


import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.openlake.sampoorna.data.di.Transformer
import org.openlake.sampoorna.data.repository.ContactsRepository
import org.openlake.sampoorna.data.repository.UserRepository
import org.openlake.sampoorna.data.sources.entities.User
import org.openlake.sampoorna.data.sources.entities.UserEntity
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository, contactsRepository: ContactsRepository):ViewModel(){
    fun insertUser(user: User)=viewModelScope.launch(Dispatchers.IO) {
        userRepository.insertUser(user)
    }
    fun updateSOSMessage(message:String) = viewModelScope.launch(Dispatchers.IO){
        userRepository.updateMessage(message)
    }
    fun userSOSMessage()=viewModelScope.launch(Dispatchers.IO){
        userRepository.fetchMessage()
    }
    fun updateLocation(location:String) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.updateLocation(location)
    }
    val userDetails = Transformations.map(userRepository.getUserDetails()){ map->
        val user = map.map {
            Transformer.convertUserEntityToUserModel(it)
        }
        user
    }
    val allContacts = Transformations.map(contactsRepository.fetchAllContacts()) { list ->
        val contacts = list.map {
            Transformer.convertContactEntityToContactModel(it)
        }
        contacts
    }
}