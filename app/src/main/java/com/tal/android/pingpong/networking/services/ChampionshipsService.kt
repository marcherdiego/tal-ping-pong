package com.tal.android.pingpong.networking.services

import com.tal.android.pingpong.domain.Championship
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChampionshipsService {

    @POST("/createChampionship.php")
    fun createChampionship(@Body championship: Championship): Call<Unit>

    @GET("/getChampionships.php")
    fun getChampionships(@Query("userId") userId: Int): Call<List<Championship>>
}
