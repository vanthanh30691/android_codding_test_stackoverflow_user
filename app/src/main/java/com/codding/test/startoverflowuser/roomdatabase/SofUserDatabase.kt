package com.codding.test.startoverflowuser.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.codding.test.startoverflowuser.modal.SoFUser
import com.codding.test.startoverflowuser.roomdatabase.dao.SofUserDao
import com.codding.test.startoverflowuser.util.RoomConstant

@Database(entities = arrayOf(SoFUser::class), version = RoomConstant.SOF_DATABASE_VERSION)
abstract class SofUserDatabase : RoomDatabase() {
    abstract fun sofUserDao() : SofUserDao

    companion object {
        @Volatile
        private var INSTANCE : SofUserDatabase? = null

        fun getDatabase(context: Context) : SofUserDatabase {
            var tempInstance = INSTANCE
            tempInstance?. let {
                return tempInstance
            }
            synchronized(this) {
                var instance = Room.databaseBuilder(context.applicationContext,
                    SofUserDatabase::class.java,
                    RoomConstant.SOF_DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}