package com.tal.android.pingpong.ui.mvp.presenter

import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.pingpong.ui.adapters.ChallengesAdapter
import com.tal.android.pingpong.ui.mvp.model.ChallengesModel
import com.tal.android.pingpong.ui.mvp.view.ChallengesView
import org.greenrobot.eventbus.Subscribe

class ChallengesPresenter(view: ChallengesView, model: ChallengesModel) :
    BaseFragmentPresenter<ChallengesView, ChallengesModel>(view, model) {

    @Subscribe
    fun onChallengesFetchedSuccessfully(event: ChallengesModel.ChallengesFetchedSuccessfullyEvent) {
        view.setChallengesAdapter(ChallengesAdapter(event.challenges, model.getUserEmail()))
    }

    @Subscribe
    fun onChallengesFetchFailed(event: ChallengesModel.ChallengesFetchFailedEvent) {

    }

    override fun onResume() {
        super.onResume()
        model.fetchUserChallenges()
    }
}
