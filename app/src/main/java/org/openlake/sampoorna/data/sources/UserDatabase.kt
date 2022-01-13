package org.openlake.sampoorna.data.sources

import androidx.room.Database
import androidx.room.RoomDatabase
import org.openlake.sampoorna.data.dao.UserDao
import org.openlake.sampoorna.data.sources.entities.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class UserDatabase:RoomDatabase() {
    abstract fun userDao():UserDao
}