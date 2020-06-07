package com.tal.android.pingpong.ui.mvp.model

import com.tal.android.pingpong.domain.Championship
import com.tal.android.pingpong.extensions.enqueueResponseNotNull
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.ChampionshipsService
import com.tal.android.pingpong.utils.SharedPreferencesUtils
import java.util.*

class EventsModel(private val sharedPreferences: SharedPreferencesUtils) : BaseModel() {
    private val championshipsService = ServiceGenerator.createService(ChampionshipsService::class.java)

    fun getUserId() = sharedPreferences.getUser()?.userId

    fun fetchChampionships() {
        championshipsService
            .getChampionships(getUserId() ?: return, startDate = Date().time, endDate = null)
            .enqueueResponseNotNull(
                success = {
                    bus.post(ChampionshipsFetchedSuccessfullyEvent(it))
                },
                fail = {
                    bus.post(ChampionshipsFetchFailedEvent())
                },
                model = this
            )
    }

    class ChampionshipsFetchedSuccessfullyEvent(val championships: List<Championship>)
    class ChampionshipsFetchFailedEvent
}
