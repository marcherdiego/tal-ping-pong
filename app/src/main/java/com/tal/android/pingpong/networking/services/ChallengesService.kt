package com.tal.android.pingpong.networking.services

import com.tal.android.pingpong.domain.Challenge
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ChallengesService {

    @POST("/challenge")
    fun challengeUser(@Body challenge: Challenge): Call<Unit>

    @GET("/challenges")
    fun getMyChallenges(): Call<List<Challenge>>
}
