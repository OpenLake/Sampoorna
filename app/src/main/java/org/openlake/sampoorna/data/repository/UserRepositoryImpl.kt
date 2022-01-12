package org.openlake.sampoorna.data.repository

import androidx.lifecycle.LiveData
import org.openlake.sampoorna.data.di.Transformer
import org.openlake.sampoorna.data.sources.UserDatabase
import org.openlake.sampoorna.data.sources.entities.User
import org.openlake.sampoorna.data.sources.entities.UserEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(private val userDatabase: UserDatabase):UserRepository{

    override suspend fun insertUser(user: User) {
        userDatabase.userDao().insert(Transformer.convertUserModelToUserEntity(user))
    }

    override suspend fun updateMessage(message: String) {
        userDatabase.userDao().update(message)
    }

    override suspend fun updateLocation(location: String) {
        userDatabase.userDao().updateLocation(location)
    }

    override fun getUserDetails(): LiveData<List<UserEntity>> {
        return userDatabase.userDao().getUserDetails()
    }
}