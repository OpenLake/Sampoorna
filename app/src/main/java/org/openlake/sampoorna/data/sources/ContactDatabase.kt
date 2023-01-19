package org.openlake.sampoorna.data.sources

import androidx.room.Database
import androidx.room.RoomDatabase
import org.openlake.sampoorna.data.dao.ContactsDao
import org.openlake.sampoorna.data.sources.entities.ContactEntity

@Database(
    entities = [ContactEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ContactDatabase: RoomDatabase() {
    abstract fun contactsDao(): ContactsDao
}