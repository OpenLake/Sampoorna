package org.openlake.sampoorna.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.openlake.sampoorna.data.sources.entities.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user:UserEntity)

    @Query("UPDATE USER SET user_sos_message=:newMessage")
    fun update(newMessage:String)

    @Query("Select user_sos_message from user")
    fun getSosMessage() : String

    @Query("UPDATE user SET user_current_location=:newLocation")
    fun updateLocation(newLocation:String)

    @Query("Select user_current_location from user")
    fun getLocation(): String

    @Query("Select * from user order by id ASC")
    fun getUserDetails(): LiveData<List<UserEntity>>
}