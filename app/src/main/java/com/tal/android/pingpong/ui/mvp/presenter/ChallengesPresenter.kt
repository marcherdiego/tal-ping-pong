package com.tal.android.pingpong.ui.mvp.presenter

import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.pingpong.ui.mvp.model.ChallengesModel
import com.tal.android.pingpong.ui.mvp.view.ChallengesView

class ChallengesPresenter(view: ChallengesView, model: ChallengesModel) :
    BaseFragmentPresenter<ChallengesView, ChallengesModel>(view, model) {

    override fun onResume() {
        super.onResume()
        model.fetchUserChallenges()
    }
}
