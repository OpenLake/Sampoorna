package org.openlake.sampoorna.data.di

import org.openlake.sampoorna.data.sources.entities.Contact
import org.openlake.sampoorna.data.sources.entities.ContactEntity
import org.openlake.sampoorna.data.sources.entities.User
import org.openlake.sampoorna.data.sources.entities.UserEntity

/*
* This is a transformer class
* We cannot just use our model classes for inserting or getting data from db
* There might be a time when variables might be added or removed in model classes
* So it is always a better practice to have transformer classes
* */
object Transformer {

    fun convertContactModelToContactEntity(contact: Contact): ContactEntity {
        return ContactEntity(
            name = contact.name,
            contact = contact.contact,
            id = contact.id
        )
    }

    fun convertContactEntityToContactModel(contactEntity: ContactEntity): Contact {
        return Contact(
            name = contactEntity.name,
            contact = contactEntity.contact,
            id = contactEntity.id
        )
    }

    fun convertUserModelToUserEntity(user: User):UserEntity{
        return UserEntity(
            name = user.name,
            address = user.address,
            id = user.id,
            bloodGroup =  user.bloodGroup,
            sosMessage = user.sosMessage,
            age = user.age,
            email = user.email
        )
    }

    fun convertUserEntityToUserModel(userEntity: UserEntity):User{
        return User(
            name = userEntity.name,
            address = userEntity.address,
            id = userEntity.id,
            bloodGroup = userEntity.bloodGroup,
            sosMessage = userEntity.sosMessage,
            age = userEntity.age,
            email = userEntity.email
        )
    }
}