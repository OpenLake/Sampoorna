package org.openlake.sampoorna.data.sources.entities

data class Blog(
    val blogId: String = "",
    var title: String = "",
    var content: String = "",
    var authorUsername: String = "",
    var authorUid: String = "",
    var tags: List<String> = mutableListOf(),
    var timestamp: Long = System.currentTimeMillis(),
    val anonymous: Boolean = false
)
