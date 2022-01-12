package org.openlake.sampoorna.data.sources.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val name:String?,
    val bloodGroup:String?,
    val address:String? ,
    var sosMessage:String? = "",
    var currentLocation:String?="",
    var id: Int = 0
):Parcelable
