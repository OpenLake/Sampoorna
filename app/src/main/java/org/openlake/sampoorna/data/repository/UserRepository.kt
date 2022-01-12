package org.openlake.sampoorna.data.repository

import androidx.lifecycle.LiveData
import org.openlake.sampoorna.data.sources.entities.User
import org.openlake.sampoorna.data.sources.entities.UserEntity

interface UserRepository {
    suspend fun fetchMessage() : String
    suspend fun insertUser(user: User)
    suspend fun updateMessage(message:String)
    suspend fun updateLocation(location:String)
    suspend fun fetchLocation() : String
    fun getUserDetails() : LiveData<List<UserEntity>>
}