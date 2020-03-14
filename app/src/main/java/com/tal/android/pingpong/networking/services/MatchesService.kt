package com.tal.android.pingpong.networking.services

import com.tal.android.pingpong.domain.Match
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MatchesService {

    @POST("/challengeUser.php")
    fun challengeUser(@Body match: Match): Call<Unit>

    @GET("/getMatches.php")
    fun getMyMatches(@Query("userId") userId: Int): Call<List<Match>>
}
