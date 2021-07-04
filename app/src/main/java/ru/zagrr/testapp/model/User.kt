package ru.zagrr.testapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(

    @PrimaryKey(autoGenerate = false)
    val id : Long,

    val name : String,
    val username : String,
    val email : String?,
    val phone : String?,
    val website : String?,
)
