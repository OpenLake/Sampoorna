package org.openlake.sampoorna.data.sources.entities

data class Comment(
    var authorUid: String = "",
    var authorUsername: String = "",
    var content: String = "",
    var timestamp: Long = System.currentTimeMillis(),
    var anonymous: Boolean = false
)