package org.openlake.sampoorna.data.sources.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val name: String?,
    val contact: String?,
    var id: Int = 0
):Parcelable