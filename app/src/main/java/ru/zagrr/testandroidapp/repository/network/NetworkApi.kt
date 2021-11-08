package ru.zagrr.testandroidapp.repository.network

import retrofit2.Response
import retrofit2.http.GET
import ru.zagrr.testandroidapp.model.User

interface NetworkApi {

    @GET("/users")
    suspend fun getUsers() : Response<List<User>>

}