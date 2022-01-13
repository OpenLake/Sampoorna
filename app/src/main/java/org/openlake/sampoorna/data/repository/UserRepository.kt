package org.openlake.sampoorna.data.repository

import androidx.lifecycle.LiveData
import org.openlake.sampoorna.data.sources.entities.User
import org.openlake.sampoorna.data.sources.entities.UserEntity

interface UserRepository {
    suspend fun insertUser(user: User)
    suspend fun updateMessage(message:String)
    suspend fun updateLocation(location:String)
    fun getUserDetails() : LiveData<List<UserEntity>>
}