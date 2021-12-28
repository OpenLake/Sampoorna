package org.openlake.sampoorna.data.sources

import androidx.room.Database
import androidx.room.RoomDatabase
import org.openlake.sampoorna.models.ContactsDao
import org.openlake.sampoorna.models.ContactsEntity

@Database(entities = [ContactsEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase :RoomDatabase() {
    abstract fun contactsDao(): ContactsDao
}