package com.tal.android.pingpong.ui.mvp.presenter

import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.pingpong.ui.adapters.MatchesAdapter
import com.tal.android.pingpong.ui.mvp.model.UpcomingMatchesModel
import com.tal.android.pingpong.ui.mvp.view.UpcomingMatchesView
import org.greenrobot.eventbus.Subscribe

class UpcomingMatchesPresenter(view: UpcomingMatchesView, model: UpcomingMatchesModel) :
    BaseFragmentPresenter<UpcomingMatchesView, UpcomingMatchesModel>(view, model) {

    @Subscribe
    fun onMatchesFetchedSuccessfully(event: UpcomingMatchesModel.MatchesFetchedSuccessfullyEvent) {
        view.setRefreshing(false)
        view.setMatchesAdapter(MatchesAdapter(event.matches, model.getUserEmail()))
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
