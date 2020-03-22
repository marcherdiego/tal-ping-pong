package com.tal.android.pingpong.ui.mvp.presenter

import com.nerdscorner.mvplib.events.bus.Bus
import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.pingpong.ui.adapters.PastMatchesAdapter
import com.tal.android.pingpong.ui.mvp.model.PastMatchesModel
import com.tal.android.pingpong.ui.mvp.view.PastMatchesView
import org.greenrobot.eventbus.Subscribe

class PastMatchesPresenter(view: PastMatchesView, model: PastMatchesModel, bus: Bus) :
    BaseFragmentPresenter<PastMatchesView, PastMatchesModel>(view, model, bus) {

    @Subscribe
    fun onMatchesFetchedSuccessfully(event: PastMatchesModel.MatchesFetchedSuccessfullyEvent) {
        view.setRefreshing(false)
        view.setMatchesAdapter(PastMatchesAdapter(event.matches, model.getUserEmail()))
    }

    @Subscribe
    fun onMatchesFetchFailed(event: PastMatchesModel.MatchesFetchFailedEvent) {
        view.setRefreshing(false)
    }

    @Subscribe
    fun onRefreshMatches(event: PastMatchesView.RefreshMatchesEvent) {
        model.fetchUserMatches()
    }

    override fun onResume() {
        super.onResume()
        model.fetchUserMatches()
    }
}
