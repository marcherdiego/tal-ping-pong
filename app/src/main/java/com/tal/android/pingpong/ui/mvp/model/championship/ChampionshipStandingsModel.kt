package com.tal.android.pingpong.ui.mvp.model.championship

import com.tal.android.pingpong.domain.Standing
import com.tal.android.pingpong.extensions.enqueueResponseNotNull
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.ChampionshipsService
import com.tal.android.pingpong.ui.mvp.model.BaseModel

class ChampionshipStandingsModel(private val championshipId: Int, val doubles: Boolean) : BaseModel() {
    private val championshipsService = ServiceGenerator.createService(ChampionshipsService::class.java)

    fun fetchStandings() {
        championshipsService
            .getChampionshipStandings(championshipId)
            .enqueueResponseNotNull(
                success = { standings ->
                    bus.post(
                        StandingsFetchedSuccessfullyEvent(
                            standings.sortedDescending()
                        )
                    )
                },
                fail = {
                    bus.post(StandingsFetchFailedEvent())
                },
                model = this
            )
    }

    class StandingsFetchedSuccessfullyEvent(val standings: List<Standing>)
    class StandingsFetchFailedEvent
}
