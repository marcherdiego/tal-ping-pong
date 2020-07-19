package com.tal.android.pingpong.ui.mvp.presenter.championship

import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.pingpong.ui.adapters.recyclerview.StandingsAdapter
import com.tal.android.pingpong.ui.mvp.view.championship.ChampionshipStandingsView
import com.tal.android.pingpong.ui.mvp.model.championship.ChampionshipStandingsModel
import org.greenrobot.eventbus.Subscribe

class ChampionshipStandingsPresenter(view: ChampionshipStandingsView, model: ChampionshipStandingsModel) :
    BaseFragmentPresenter<ChampionshipStandingsView, ChampionshipStandingsModel>(view, model) {

    @Subscribe
    fun onStandingsFetchedSuccessfully(event: ChampionshipStandingsModel.StandingsFetchedSuccessfullyEvent) {
        view.setRefreshing(false)
        view.setStandingsAdapter(
            StandingsAdapter(
                model.doubles,
                event.standings
            )
        )
    }

    @Subscribe
    fun onStandingsFetchFailed(event: ChampionshipStandingsModel.StandingsFetchFailedEvent) {
        view.setRefreshing(false)
        view.showNetworkErrorMessage()
    }

    @Subscribe
    fun onRefreshStandings(event: ChampionshipStandingsView.RefreshStandingsEvent) {
        model.fetchStandings()
    }

    override fun onResume() {
        super.onResume()
        model.fetchStandings()
    }
}
