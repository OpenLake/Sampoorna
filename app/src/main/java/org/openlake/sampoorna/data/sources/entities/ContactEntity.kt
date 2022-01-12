package org.openlake.sampoorna.data.sources.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CONTACTS")
data class ContactEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val name: String?,
    val contact: String?
)