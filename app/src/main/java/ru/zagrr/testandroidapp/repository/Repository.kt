package ru.zagrr.testandroidapp.repository

import android.util.Log
import retrofit2.Response
import ru.zagrr.testandroidapp.model.User
import ru.zagrr.testandroidapp.repository.db.DocumentDao
import ru.zagrr.testandroidapp.repository.network.NetworkApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val networkApi: NetworkApi, private val documentDao: DocumentDao) {

    private fun <T> getErrorMessage(response: Response<T>) : String {

        val err = response.errorBody()?.string()

        if (err?.isNotEmpty() == true)
            return err

        else {
            val responseMessage = response.message()

            if (responseMessage.isNotEmpty())
                return responseMessage
        }

        return "Неизвестная ошибка";
    }

    fun getUsers(searchQuery : String) = documentDao.getUsers(searchQuery)

    suspend fun refreshUsers() {

        val response = networkApi.getUsers()

        if (response.isSuccessful) {

            val users = response.body() ?: throw Exception("Неизвестная ошибка")
            documentDao.refreshUsers(users)
        }
        else
            throw Exception(
                getErrorMessage(response)
            )
    }

    suspend fun deleteUsers(users: List<User>) = documentDao.deleteUsers(users)

    suspend fun saveUsers(users: List<User>) = documentDao.saveUsers(users)

}