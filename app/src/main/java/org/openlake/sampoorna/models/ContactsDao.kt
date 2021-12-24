package org.openlake.sampoorna.models

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(contact:Contacts)

    @Delete
    suspend fun delete(note:Contacts)

    @Query("Select * from contact_list order by id ASC")
    fun getAllContacts(): LiveData<List<Contacts>>

}