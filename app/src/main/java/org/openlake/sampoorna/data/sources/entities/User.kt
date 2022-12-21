package org.openlake.sampoorna.data.sources.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val uid: String = "",
    val name: String = "",
    var username: String = "",
    var email: String = "",
    var photoUrl: String? = null,
    var about: String = "",
    var age: Int? = null
):Parcelable
