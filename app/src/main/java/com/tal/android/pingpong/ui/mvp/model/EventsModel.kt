package com.tal.android.pingpong.ui.mvp.model

import com.tal.android.pingpong.domain.Championship
import com.tal.android.pingpong.extensions.enqueueResponseNotNull
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.ChampionshipsService
import com.tal.android.pingpong.utils.SharedPreferencesUtils
import java.util.*

class EventsModel(private val sharedPreferences: SharedPreferencesUtils, private var selectedEvent: Int) : BaseModel() {
    private val championshipsService = ServiceGenerator.createService(ChampionshipsService::class.java)

    val championships = mutableListOf<Championship>()

    fun getUserId() = sharedPreferences.getUser()?.userId

    fun fetchChampionships() {
        championshipsService
            .getChampionships(getUserId() ?: return, startDate = Date().time, endDate = null)
            .enqueueResponseNotNull(
                success = {
                    championships.clear()
                    championships.addAll(it)
                    bus.post(ChampionshipsFetchedSuccessfullyEvent())
                },
                fail = {
                    bus.post(ChampionshipsFetchFailedEvent())
                },
                model = this
            )
    }

    fun getSelectedChampionship(): Championship? {
        val championship = championships.firstOrNull { it.championshipId == selectedEvent }
        selectedEvent = UNSET
        return championship
    }

    class ChampionshipsFetchedSuccessfullyEvent
    class ChampionshipsFetchFailedEvent

    companion object {
        const val UNSET = -1
    }
}
