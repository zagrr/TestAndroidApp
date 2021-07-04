package ru.zagrr.testapp.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.zagrr.testapp.model.User

@Database(entities = [User::class,], version = 1, exportSchema = false)
abstract class TestRoomDatabase : RoomDatabase(){

    abstract fun documentDao(): DocumentDao
}