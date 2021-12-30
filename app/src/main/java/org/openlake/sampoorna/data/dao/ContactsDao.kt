package org.openlake.sampoorna.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.openlake.sampoorna.data.sources.entities.ContactsEntity

@Dao
interface ContactsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(contact: ContactsEntity)

    @Delete
    fun delete(note: ContactsEntity)

    @Query("Select * from contacts order by id ASC")
    fun getAllContacts(): LiveData<List<ContactsEntity>>

}