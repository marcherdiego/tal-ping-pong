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
        view.setRefreshing(false)
        view.setStandingsAdapter(StandingsAdapter(event.standings))
    }

    @Subscribe
    fun onStandingsFetchFailed(event: StandingsModel.StandingsFetchFailedEvent) {
        view.setRefreshing(false)
        view.showNetworkErrorMessage()
    }

    @Subscribe
    fun onRefreshStandings(event: StandingsView.RefreshStandingsEvent) {
        model.fetchStandings()
    }

    override fun onResume() {
        super.onResume()
        model.fetchStandings()
    }
}