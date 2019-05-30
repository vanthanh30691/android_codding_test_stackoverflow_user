package com.codding.test.startoverflowuser.roomdatabase.dao

import androidx.room.*
import com.codding.test.startoverflowuser.modal.SoFUser

@Dao
interface SofUserDao {

    @Query("SELECT * from SofUser")
    fun getAllFavoriteUser() : List<SoFUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUserToFavorite(soFUser: SoFUser)

    @Delete
    suspend fun removeUserFromFavorite(soFUser: SoFUser)
}