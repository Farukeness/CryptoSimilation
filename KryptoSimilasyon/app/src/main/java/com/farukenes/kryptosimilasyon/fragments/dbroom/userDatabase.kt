package com.farukenes.kryptosimilasyon.fragments.dbroom

import androidx.room.Dao
import androidx.room.Database
import androidx.room.RoomDatabase
import com.farukenes.kryptosimilasyon.fragments.model.userData

@Database(entities = [userData::class], version = 1)
abstract class userDatabase : RoomDatabase() {
    abstract fun userDao(): userDao
}
