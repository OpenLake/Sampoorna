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

    @Query("UPDATE user SET user_sos_message=:newMessage")
    fun update(newMessage:String)

    @Query("UPDATE user SET user_address=:newAddress")
    fun updateAddress(newAddress:String)

    @Query("UPDATE user SET user_age=:newAge")
    fun updateAge(newAge:Int)

    @Query("UPDATE user SET user_bloodgroup=:bloodGroup")
    fun updateBloodGroup(bloodGroup:String)

    @Query("UPDATE user SET user_email=:newEmail")
    fun updateEmail(newEmail:String)

    @Query("UPDATE user SET user_name=:name")
    fun updateName(name:String)

    @Query("Select * from user order by id ASC")
    fun getUserDetails(): LiveData<List<UserEntity>>
}