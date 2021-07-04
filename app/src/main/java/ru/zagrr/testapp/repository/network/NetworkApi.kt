package ru.zagrr.testapp.repository.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.zagrr.testapp.model.User

interface NetworkApi {

    @GET("/users")
    suspend fun getUsers() : Response<List<User>>

}