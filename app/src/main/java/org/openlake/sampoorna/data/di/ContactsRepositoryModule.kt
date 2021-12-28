package org.openlake.sampoorna.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import org.openlake.sampoorna.data.repository.ContactsRepository
import org.openlake.sampoorna.data.repository.ContactsRepositoryImpl

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ContactsRepositoryModule {
    @Binds
    abstract fun bindsContactsRepository(repository: ContactsRepositoryImpl?): ContactsRepository?
}