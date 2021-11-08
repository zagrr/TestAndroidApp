package ru.zagrr.testandroidapp.repository.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.zagrr.testandroidapp.model.User

@Dao
interface DocumentDao {

    @Transaction
    suspend fun refreshUsers(users: List<User>) {
        deleteAllUsers()
        saveUsers(users)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUsers(users: List<User>)

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    @Delete
    suspend fun deleteUsers(users : List<User>)

    @Query("SELECT * FROM users WHERE name LIKE '%' || :searchQuery || '%'")
    fun getUsers(searchQuery : String): Flow<List<User>>

}