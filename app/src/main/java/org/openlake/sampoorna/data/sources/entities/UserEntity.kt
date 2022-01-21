package org.openlake.sampoorna.data.sources.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "USER")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "user_name")
    val name:String?,
    @ColumnInfo(name="user_bloodgroup")
    val bloodGroup:String?,
    @ColumnInfo(name = "user_address")
    val address:String?,
    @ColumnInfo(name = "user_sos_message")
    var sosMessage:String? = "",
    @ColumnInfo(name = "user_age")
    var age:Int?,
    @ColumnInfo(name = "user_email")
    var email:String?
)