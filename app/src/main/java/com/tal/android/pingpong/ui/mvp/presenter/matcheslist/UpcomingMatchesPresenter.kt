package com.tal.android.pingpong.ui.mvp.presenter.matcheslist

import com.nerdscorner.mvplib.events.bus.Bus
import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.pingpong.ui.adapters.UpcomingMatchesAdapter
import com.tal.android.pingpong.ui.mvp.model.matcheslist.UpcomingMatchesModel
import com.tal.android.pingpong.ui.mvp.view.matcheslist.UpcomingMatchesView
import org.greenrobot.eventbus.Subscribe

class UpcomingMatchesPresenter(view: UpcomingMatchesView, model: UpcomingMatchesModel, bus: Bus) :
    BaseFragmentPresenter<UpcomingMatchesView, UpcomingMatchesModel>(view, model, bus) {

    @Subscribe
    fun onMatchesFetchedSuccessfully(event: UpcomingMatchesModel.MatchesFetchedSuccessfullyEvent) {
        view.setRefreshing(false)
        view.setMatchesAdapter(UpcomingMatchesAdapter(event.matches, model.getUserEmail()))
    }

    @Subscribe
    fun onMatchesFetchFailed(event: UpcomingMatchesModel.MatchesFetchFailedEvent) {
        view.setRefreshing(false)
    }

    @Subscribe
    fun onRefreshMatches(event: UpcomingMatchesView.RefreshMatchesEvent) {
        model.fetchUserMatches()
    }

    override fun onResume() {
        super.onResume()
        model.fetchUserMatches()
    }
}
