package com.mosamir.gdscs3

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    fun login(email: String, password: String): User?

    @Insert
    fun register(user: User)

    @Query("SELECT * FROM user WHERE email = :email")
    fun getUserByEmail(email: String): User?

    @Update
    fun updateUser(user: User)

}