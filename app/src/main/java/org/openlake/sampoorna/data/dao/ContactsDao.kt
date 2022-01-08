package org.openlake.sampoorna.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.openlake.sampoorna.data.sources.entities.ContactEntity

@Dao
interface ContactsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(contact: ContactEntity)

    @Delete
    fun delete(contact: ContactEntity)

    @Query("Select * from contacts order by id ASC")
    fun getAllContacts(): LiveData<List<ContactEntity>>

}