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

    override suspend fun updateName(name: String) {
        userDatabase.userDao().updateName(name)
    }

    override suspend fun updateBloodGroup(blood: String) {
        userDatabase.userDao().updateBloodGroup(blood)
    }

    override suspend fun updateAddress(address: String){
        userDatabase.userDao().updateAddress(address)
    }

    override suspend fun updateEmail(email: String) {
        userDatabase.userDao().updateEmail(email)
    }

    override suspend fun updateAge(age: Int) {
        userDatabase.userDao().updateAge(age)
    }

    override fun getUserDetails(): LiveData<List<UserEntity>> {
        return userDatabase.userDao().getUserDetails()
    }
}