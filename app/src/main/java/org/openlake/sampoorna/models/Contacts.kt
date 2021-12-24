package org.openlake.sampoorna.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_list")
class Contacts(@ColumnInfo(name = "name") val name:String,
               @ColumnInfo(name = "contact_number")val contact: Int) {
                @PrimaryKey(autoGenerate = true) var id=0


}