package org.openlake.sampoorna.data.constants

import java.util.*

object Constants {
    /**
     * Firebase collections
     */
    const val Users = "users"
    const val Blogs = "blogs"
    const val Comments = "comments"

    /**
     * Shared preference constants
     */
    const val Sampoorna = "sampoorna"
    const val SOSMessage = "sosmessage"
    const val Name = "name"
    const val Username = "username"
    const val Age = "age"
    const val Email = "email"
    const val About = "about"

    /**
     * Other useful constants
     */
    val months = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    
    fun getDateString(timestamp: Long): String {
        val date = Date(timestamp)
        val hours = date.hours
        val mins = date.minutes
        val time = "${if(hours > 9) "" else "0"}${hours}:${if(mins > 9) "" else "0"}${mins}"
        val dateString = "${if(date.date > 9) "" else "0"}${date.date}/${if(date.month > 8) "" else "0"}${date.month + 1}/${date.year + 1900}"
        return "$time, $dateString"
    }
}