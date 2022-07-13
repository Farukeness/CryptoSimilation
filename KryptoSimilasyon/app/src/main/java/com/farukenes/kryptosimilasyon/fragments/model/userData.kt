package com.farukenes.kryptosimilasyon.fragments.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class userData(
    @ColumnInfo(name="bakiye")
    var bakiye:Int
    ) {
    @PrimaryKey(autoGenerate = true)
        var id = 0

}