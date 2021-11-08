package ru.zagrr.testandroidapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "users")
@Parcelize
data class User(

    @PrimaryKey(autoGenerate = false)
    val id : Long,

    val name : String,
    val username : String,
    val email : String?,
    val phone : String?,
    val website : String?,
): Parcelable
