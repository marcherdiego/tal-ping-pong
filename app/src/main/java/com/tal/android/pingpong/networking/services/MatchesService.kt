package com.tal.android.pingpong.networking.services

import com.tal.android.pingpong.domain.Match
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.domain.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.*

interface MatchesService {

    @POST("/challengeUser.php")
    fun challengeUser(@Body match: MatchRecord): Call<Unit>

    @POST("/acceptChallenge.php")
    fun acceptChallenge(@Body match: MatchRecord): Call<Unit>

    @POST("/declineChallenge.php")
    fun declineChallenge(@Query("userId") userId: Int, @Body match: MatchRecord, @Query("declineReason") declineReason: String): Call<Unit>

    @POST("/editMatch.php")
    fun editMatch(@Query("userId") userId: Int, @Body match: MatchRecord): Call<Unit>

    @POST("/acceptMatchEdit.php")
    fun acceptMatchEdit(@Query("userId") userId: Int, @Body match: MatchRecord): Call<Unit>

    @POST("/declineMatchEdit.php")
    fun declineMatchEdit(@Query("userId") userId: Int, @Body match: MatchRecord): Call<Unit>

    @GET("/getMatches.php")
    fun getMyMatches(
        @Query("userId") userId: Int,
        @Query("startDate") startDate: Long? = null,
        @Query("endDate") endDate: Long? = null,
        @Query("confirmed") confirmed: Boolean = true,
        @Query("fetchMatchHistory") fetchMatchHistory: Boolean = false
    ): Call<List<Match>>

    @GET("/getLastMatches.php")
    fun getLastTenMatches(@Query("userId") userId: Int): Call<List<Match>>
}
