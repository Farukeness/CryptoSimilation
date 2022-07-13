package com.farukenes.kryptosimilasyon.fragments.dbroom

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.farukenes.kryptosimilasyon.fragments.model.userData
import io.reactivex.rxjava3.core.Completable

@Dao
interface userDao {
    @Insert
    fun insert(userData: userData)

    @Delete
    fun delete(userData: userData)

    @Query("SELECT * FROM userData")
    fun getAll():List<userData>
}