package ru.zagrr.testapp.repository.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.zagrr.testapp.model.User

@Dao
interface DocumentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUsers(users: List<User>)

    @Query("SELECT * FROM users")
    fun getUsers(): Flow<List<User>>

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

}