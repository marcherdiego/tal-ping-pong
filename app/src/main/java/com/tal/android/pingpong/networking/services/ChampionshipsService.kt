package com.tal.android.pingpong.networking.services

import com.tal.android.pingpong.domain.Championship
import com.tal.android.pingpong.domain.Match
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.domain.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChampionshipsService {

    @POST("/createChampionship.php")
    fun createChampionship(@Body championship: Championship): Call<Unit>

    @GET("/getChampionships.php")
    fun getChampionships(
        @Query("userId") userId: Int,
        @Query("startDate") startDate: Long? = null,
        @Query("endDate") endDate: Long? = null
    ): Call<List<Championship>>

    @GET("/getChampionshipMatches.php")
    fun getChampionshipMatches(@Query("championshipId") championshipId: Int): Call<List<Match>>

    @GET("/getChampionshipMembers.php")
    fun getChampionshipMembers(@Query("championshipId") championshipId: Int): Call<List<User>>
}
