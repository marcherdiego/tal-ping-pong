package com.tal.android.pingpong.ui.mvp.presenter

import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.pingpong.ui.adapters.ChampionshipsAdapter
import com.tal.android.pingpong.ui.mvp.view.EventsView
import com.tal.android.pingpong.ui.mvp.model.EventsModel
import org.greenrobot.eventbus.Subscribe

class EventsPresenter(view: EventsView, model: EventsModel) :
    BaseFragmentPresenter<EventsView, EventsModel>(view, model) {

    @Subscribe
    fun onChampionshipsFetchedSuccessfully(event: EventsModel.ChampionshipsFetchedSuccessfullyEvent) {
        view.setRefreshing(false)
        view.setChampionshipsAdapter(ChampionshipsAdapter(event.championships, model.getBus()))
    }

    @Subscribe
    fun onChampionshipsFetchFailed(event: EventsModel.ChampionshipsFetchFailedEvent) {
        view.setRefreshing(false)
        view.showNetworkErrorMessage()
    }

    @Subscribe
    fun onRefreshChampionships(event: EventsView.RefreshChampionshipsEvent) {
        model.fetchChampionships()
    }

    override fun onResume() {
        super.onResume()
        view.setRefreshing(true)
        model.fetchChampionships()
    }
}
