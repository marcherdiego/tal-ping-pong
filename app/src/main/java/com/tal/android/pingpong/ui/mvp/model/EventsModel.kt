package com.tal.android.pingpong.ui.mvp.model

import com.tal.android.pingpong.domain.Championship
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.extensions.enqueue
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.ChampionshipsService
import com.tal.android.pingpong.utils.SharedPreferencesUtils

class EventsModel(private val sharedPreferences: SharedPreferencesUtils) : BaseModel() {
    private val championshipsService = ServiceGenerator.createService(ChampionshipsService::class.java)

    fun getUserId() = sharedPreferences.getUser()?.userId

    fun fetchChampionships() {
        bus.post(
            ChampionshipsFetchedSuccessfullyEvent(
                mutableListOf<Championship>().apply {
                    add(Championship(
                        championshipDate = "2020-03-23 11:02:00",
                        championshipName = "Culo",
                        championshipImage = "https://yugen-collectibles.com/3758-large_default/marvel-fantastic-four-the-thing-pop.jpg",
                        creator = sharedPreferences.getUser(),
                        users = mutableListOf<User>().apply {
                            add(sharedPreferences.getUser()!!)
                            add(sharedPreferences.getUser()!!)
                            add(sharedPreferences.getUser()!!)
                            add(sharedPreferences.getUser()!!)
                            add(sharedPreferences.getUser()!!)
                            add(sharedPreferences.getUser()!!)
                            add(sharedPreferences.getUser()!!)
                            add(sharedPreferences.getUser()!!)
                        }
                    ))
                }
            )
        )
        championshipsService
            .getChampionships(getUserId() ?: return)
            .enqueue(
                success = {
                    //bus.post(ChampionshipsFetchedSuccessfullyEvent())
                },
                fail = {
                    //bus.post(ChampionshipsFetchFailedEvent())
                },
                model = this
            )
    }

    class ChampionshipsFetchedSuccessfullyEvent(val championships: List<Championship>)
    class ChampionshipsFetchFailedEvent
}
