package com.example.rbc_app.RoomDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.rbc_app.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll():List<UserCache>

    @Query("SELECT User_id FROM user LIMIT 1")  // Query to get the first row
    suspend fun getUid(): Int?

    @Query("SELECT first_name FROM user")
    fun getFirstName():String

    @Query("SELECT last_name FROM user")
    fun getLastName():String

//    @Query("SELECT Image FROM user")
//    fun getImageByte():ByteArray

    @Query("SELECT Email FROM user")
    fun getEmail():String

    @Query("SELECT phone FROM user")
    fun getphone():String

    @Insert
    suspend fun insertUser(user: UserCache)

    @Query("SELECT SavedJobs FROM user WHERE uid = 1")
    suspend fun getSavedJobs(): String

    @Query("SELECT SavedFreelance FROM user WHERE uid = 1")
    suspend fun getSavedFreelance(): String

    @Query("SELECT SavedInternships FROM user WHERE uid = 1")
    suspend fun getSavedInternships(): String

    // Update the string in the same row
    @Query("UPDATE user SET SavedJobs = :newJobs WHERE uid = 1")
    suspend fun updateSavedJobs(newJobs: String)

    @Query("UPDATE user SET savedInternships = :newInternship WHERE uid = 1")
    suspend fun UpdateSavedInternships(newInternship: String)


    @Query("UPDATE user SET SavedFreelance = :newFreelance WHERE uid = 1")
    suspend fun updateSavedFreelance(newFreelance: String)

    @Query("SELECT COUNT(*) FROM user")
    suspend fun chk():Int

    @Delete
    fun delete(user:UserCache)
}