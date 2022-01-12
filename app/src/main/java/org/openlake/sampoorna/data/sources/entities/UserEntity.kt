package org.openlake.sampoorna.data.sources.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "USER")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val name:String?,
    val bloodGroup:String?,
    val address:String?,
    @ColumnInfo(name = "user_sos_message")
    var sosMessage:String? = "",
    @ColumnInfo(name = "user_current_location")
    var currentLocation:String?=""
)