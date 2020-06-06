package com.tal.android.pingpong.networking.services

import com.tal.android.pingpong.domain.Championship
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ChampionshipsService {

    @POST("/createChampionship.php")
    fun createChampionship(@Body championship: Championship): Call<Unit>
}
