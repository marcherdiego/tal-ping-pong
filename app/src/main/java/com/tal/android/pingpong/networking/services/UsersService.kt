package com.tal.android.pingpong.networking.services

import com.tal.android.pingpong.domain.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UsersService {

    @POST("/user")
    fun login(@Body user: User): Call<Unit>

    @GET("/users")
    fun getUsers(): Call<List<User>>
}
