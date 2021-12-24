package org.openlake.sampoorna.models

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class ContactsDatabase:RoomDatabase() {
    abstract  fun getContactsDao(): ContactsDao

    companion object{
        @Volatile
        private  var INSTANCE: ContactsDatabase?=null
        fun getDatabase(context: Context): ContactsDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContactsDatabase::class.java,
                    "contacts_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}