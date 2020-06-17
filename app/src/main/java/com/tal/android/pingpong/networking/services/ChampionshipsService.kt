package com.tal.android.pingpong.networking.services

import com.tal.android.pingpong.domain.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChampionshipsService {

    @POST("/createChampionship.php")
    fun createChampionship(
        @Query("userId") userId: Int,
        @Body championship: Championship
    ): Call<Unit>

    @POST("/createChampionshipMatch.php")
    fun createChampionshipMatch(
        @Query("userId") userId: Int,
        @Query("championshipId") championshipId: Int,
        @Body match: MatchRecord
    ): Call<Unit>

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

    @GET("/getChampionshipStandings.php")
    fun getChampionshipStandings(@Query("championshipId") championshipId: Int): Call<List<Standing>>
}
