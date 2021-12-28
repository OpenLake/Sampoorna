package org.openlake.sampoorna.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contacts(
    val name: String?,
    val contact: Int?
):Parcelable