package com.tal.android.pingpong.ui.mvp.model

import com.nerdscorner.mvplib.events.model.BaseEventsModel
import com.tal.android.pingpong.extensions.enqueueResponseNotNull
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.ChallengesService

class ChallengesModel : BaseEventsModel() {

    private val challengesService = ServiceGenerator.createService(ChallengesService::class.java)

    fun fetchUserChallenges() {
        challengesService
            .getMyChallenges()
            .enqueueResponseNotNull(
                success = {
                    bus.post(ChallengesFetchedSuccessfullyEvent())
                },
                fail = {
                    bus.post(ChallengesFetchFailedEvent())
                }
            )
    }

    class ChallengesFetchedSuccessfullyEvent
    class ChallengesFetchFailedEvent
}
