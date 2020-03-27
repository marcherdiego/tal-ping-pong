package com.tal.android.pingpong.ui.mvp.presenter.matcheslist

import com.nerdscorner.mvplib.events.bus.Bus
import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.pingpong.ui.mvp.model.matcheslist.BaseMatchesListModel
import com.tal.android.pingpong.ui.mvp.view.matcheslist.BaseMatchesListView
import org.greenrobot.eventbus.Subscribe

abstract class BaseMatchesListPresenter<V : BaseMatchesListView, M : BaseMatchesListModel>(view: V, model: M, bus: Bus) :
    BaseFragmentPresenter<V, M>(view, model, bus) {

    @Subscribe
    fun onMatchesFetchFailed(event: BaseMatchesListModel.MatchesFetchFailedEvent) {
        view.setRefreshing(false)
    }

    @Subscribe
    fun onRefreshMatches(event: BaseMatchesListView.RefreshMatchesEvent) {
        model.fetchUserMatches()
    }

    override fun onResume() {
        super.onResume()
        model.fetchUserMatches()
    }
}