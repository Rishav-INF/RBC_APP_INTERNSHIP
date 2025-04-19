package com.example.rbc_app.RoomDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserCache (
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "first_name") val firstName:String?,
    @ColumnInfo(name="last_name") val lastname:String?,
    @ColumnInfo(name = "User_id") val User_id:Int?,
//    @ColumnInfo(name="Image") val imagebyte : ByteArray?,
    @ColumnInfo(name="Email") val email : String?,
    @ColumnInfo(name="phone") val phone : String?
)