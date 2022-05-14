package org.openlake.sampoorna.presentation.features.profile

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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
}