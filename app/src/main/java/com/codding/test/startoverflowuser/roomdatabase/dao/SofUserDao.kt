package com.codding.test.startoverflowuser.roomdatabase.dao

import androidx.room.*
import com.codding.test.startoverflowuser.modal.SoFUser
import com.codding.test.startoverflowuser.util.ApiConstant

@Dao
interface SofUserDao {

    @Query("SELECT * from SofUser")
    fun getAllFavoriteUser() : MutableList<SoFUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUserToFavorite(soFUser: SoFUser)

    @Delete
    suspend fun removeUserFromFavorite(soFUser: SoFUser)

    @Query("SELECT user_id from SofUser")
    fun getSofFavoriteIdList() : List<String>

    @Query("SELECT * from SofUser where user_id = :userId")
    fun getUserById(userId : String) : SoFUser
}