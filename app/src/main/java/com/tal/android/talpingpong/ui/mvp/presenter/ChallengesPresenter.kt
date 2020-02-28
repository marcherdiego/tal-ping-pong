package com.tal.android.talpingpong.ui.mvp.presenter

import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.talpingpong.ui.mvp.view.ChallengesView
import com.tal.android.talpingpong.ui.mvp.model.ChallengesModel

class ChallengesPresenter(view: ChallengesView, model: ChallengesModel) :
    BaseFragmentPresenter<ChallengesView, ChallengesModel>(view, model) {

}
