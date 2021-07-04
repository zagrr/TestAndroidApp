package ru.zagrr.testapp.repository

import android.util.Log
import retrofit2.Response
import ru.zagrr.testapp.repository.db.DocumentDao
import ru.zagrr.testapp.repository.network.NetworkApi
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

    fun getUsers() = documentDao.getUsers()

    suspend fun refreshUsers() {

        Log.d("zagrrLog", "Получаем пользователей с сервера...")

        val response = networkApi.getUsers()

        if (response.isSuccessful) {

            val users = response.body() ?: throw Exception("Неизвестная ошибка")

            Log.d("zagrrLog", "Сохраняем в локальную БД...")
            documentDao.saveUsers(users)
        }
        else
            throw Exception(
                getErrorMessage(response)
            )
    }

    suspend fun deleteAllUsers() {
        documentDao.deleteAllUsers()
    }
}