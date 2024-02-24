package com.mosamir.gdscs3

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0,
    val userName: String,
    val password: String,
    val email: String,
    val age: String,
    var questionnaire: Int = 0
):Serializable

data class Questions(
    val question: String,
    val answer: List<String>
)
