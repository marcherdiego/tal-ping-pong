package com.tal.android.pingpong.ui.mvp.presenter.matcheslist

import com.nerdscorner.mvplib.events.bus.Bus
import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.pingpong.events.MatchesUpdatedEvent
import com.tal.android.pingpong.ui.dialogs.LoadingDialog
import com.tal.android.pingpong.ui.mvp.model.matcheslist.BaseMatchesListModel
import com.tal.android.pingpong.ui.mvp.view.matcheslist.BaseMatchesListView
import org.greenrobot.eventbus.Subscribe

abstract class BaseMatchesListPresenter<V : BaseMatchesListView, M : BaseMatchesListModel>(view: V, model: M, bus: Bus) :
    BaseFragmentPresenter<V, M>(view, model, bus) {

    private var loadingDialog: LoadingDialog? = null

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

    protected fun startLoading() {
        val context = view.context ?: return
        loadingDialog = LoadingDialog().show(context)
    }

    protected fun stopLoading() {
        loadingDialog?.dismiss()
    }

    override fun onResume() {
        super.onResume()
        view.setRefreshing(true)
        model.fetchMatches()
    }
}