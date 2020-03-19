package com.tal.android.pingpong.networking.services

import com.tal.android.pingpong.domain.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UsersService {

    @POST("/login.php")
    fun login(@Body user: User): Call<User>

    @POST("/setPushToken.php")
    fun setPushToken(@Body user: User): Call<User>

    @POST("/logout.php")
    fun logout(@Body user: User): Call<Any>

    @GET("/getUsers.php")
    fun getUsers(): Call<List<User>>

    @GET("/getRanking.php")
    fun getRanking(): Call<List<User>>
}
