package com.tal.android.pingpong.ui.mvp.presenter.matcheslist

import com.nerdscorner.mvplib.events.bus.Bus
import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.pingpong.events.MatchesUpdatedEvent
import com.tal.android.pingpong.ui.mvp.model.matcheslist.BaseMatchesListModel
import com.tal.android.pingpong.ui.mvp.view.matcheslist.BaseMatchesListView
import org.greenrobot.eventbus.Subscribe

abstract class BaseMatchesListPresenter<V : BaseMatchesListView, M : BaseMatchesListModel>(view: V, model: M, bus: Bus) :
    BaseFragmentPresenter<V, M>(view, model, bus) {

    @Subscribe
    fun onMatchesFetchFailed(event: BaseMatchesListModel.MatchesFetchFailedEvent) {
        view.setRefreshing(false)
        view.showNetworkErrorMessage()
    }

    @Subscribe
    fun onRefreshMatches(event: BaseMatchesListView.RefreshMatchesEvent) {
        model.fetchMatches()
    }

    @Subscribe
    fun onMatchesUpdated(event: MatchesUpdatedEvent) {
        view.setRefreshing(true)
        model.fetchMatches()
    }

    override fun onResume() {
        super.onResume()
        view.setRefreshing(true)
        model.fetchMatches()
    }
}