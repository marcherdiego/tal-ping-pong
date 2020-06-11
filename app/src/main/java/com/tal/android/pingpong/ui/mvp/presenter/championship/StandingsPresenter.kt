package com.tal.android.pingpong.ui.mvp.presenter.championship

import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.pingpong.ui.adapters.StandingsAdapter
import com.tal.android.pingpong.ui.mvp.view.championship.StandingsView
import com.tal.android.pingpong.ui.mvp.model.championship.StandingsModel
import org.greenrobot.eventbus.Subscribe

class StandingsPresenter(view: StandingsView, model: StandingsModel) :
    BaseFragmentPresenter<StandingsView, StandingsModel>(view, model) {

    @Subscribe
    fun onStandingsFetchedSuccessfully(event: StandingsModel.StandingsFetchedSuccessfullyEvent) {
        view.setStandingsAdapter(StandingsAdapter(event.standings))
    }

    @Subscribe
    fun onStandingsFetchFailed(event: StandingsModel.StandingsFetchFailedEvent) {
        view.showNetworkErrorMessage()
    }

    override fun onResume() {
        super.onResume()
        model.fetchStandings()
    }
}
