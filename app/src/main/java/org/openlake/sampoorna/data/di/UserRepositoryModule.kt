package org.openlake.sampoorna.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import org.openlake.sampoorna.data.repository.UserRepository
import org.openlake.sampoorna.data.repository.UserRepositoryImpl

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class UserRepositoryModule {
    @Binds
    abstract fun bindsUserRepository(repository: UserRepositoryImpl?): UserRepository?
}