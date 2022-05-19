package org.openlake.sampoorna.presentation.features.profile

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.openlake.sampoorna.data.di.Transformer
import org.openlake.sampoorna.data.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    val userDetails = Transformations.map(userRepository.getUserDetails()){ map->
        val user = map.map {
            Transformer.convertUserEntityToUserModel(it)
        }
        user[0]
    }

    fun updateName(name : String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateName(name)
        }
    }

    fun updateAge(age : Int)
    {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateAge(age)
        }
    }

    fun updateEmail(email : String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateEmail(email)
        }
    }

    fun updateAddress(address : String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateAddress(address)
        }
    }

    fun updateBloodGroup(bgrp : String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateBloodGroup(bgrp)
        }
    }
}