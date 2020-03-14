package com.tal.android.pingpong.ui.mvp.presenter

import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.pingpong.ui.adapters.MatchesAdapter
import com.tal.android.pingpong.ui.mvp.model.MatchesModel
import com.tal.android.pingpong.ui.mvp.view.MatchesView
import org.greenrobot.eventbus.Subscribe

class MatchesPresenter(view: MatchesView, model: MatchesModel) :
    BaseFragmentPresenter<MatchesView, MatchesModel>(view, model) {

    @Subscribe
    fun onMatchesFetchedSuccessfully(event: MatchesModel.MatchesFetchedSuccessfullyEvent) {
        view.setRefreshing(false)
        view.setMatchesAdapter(MatchesAdapter(event.matches, model.getUserEmail()))
    }

    @Subscribe
    fun onMatchesFetchFailed(event: MatchesModel.MatchesFetchFailedEvent) {
        view.setRefreshing(false)
    }

    @Subscribe
    fun onRefreshMatches(event: MatchesView.RefreshMatchesEvent) {
        model.fetchUserMatches()
    }

    override fun onResume() {
        super.onResume()
        model.fetchUserMatches()
    }
}
