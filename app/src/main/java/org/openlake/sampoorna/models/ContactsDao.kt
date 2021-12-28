package org.openlake.sampoorna.models

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(contact:ContactsEntity)

    @Delete
    fun delete(note:ContactsEntity)

    @Query("Select * from contacts order by id ASC")
    fun getAllContacts(): LiveData<List<ContactsEntity>>

}