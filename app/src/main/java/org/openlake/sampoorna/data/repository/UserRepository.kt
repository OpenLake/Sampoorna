package org.openlake.sampoorna.data.repository

import androidx.lifecycle.LiveData
import org.openlake.sampoorna.data.sources.entities.User
import org.openlake.sampoorna.data.sources.entities.UserEntity

interface UserRepository {
    suspend fun insertUser(user: User)
    suspend fun updateMessage(message:String)
    suspend fun updateName(name:String)
    suspend fun updateBloodGroup(blood:String)
    suspend fun updateAddress(address:String)
    suspend fun updateEmail(email:String)
    suspend fun updateAge(age:Int)
    fun getUserDetails() : LiveData<List<UserEntity>>
}