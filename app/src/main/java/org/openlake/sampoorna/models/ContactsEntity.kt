package org.openlake.sampoorna.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CONTACTS")
data class ContactsEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val name: String?,
    val contact: Int?,
)