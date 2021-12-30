package org.openlake.sampoorna.data.sources.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contacts(
    val name: String?,
    val contact: String?
):Parcelable