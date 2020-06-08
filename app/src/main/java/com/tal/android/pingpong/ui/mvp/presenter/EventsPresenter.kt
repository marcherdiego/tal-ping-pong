package com.tal.android.pingpong.ui.mvp.presenter

import androidx.core.os.bundleOf
import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.pingpong.ui.activities.ChampionshipActivity
import com.tal.android.pingpong.ui.activities.NewChampionshipActivity
import com.tal.android.pingpong.ui.adapters.recyclerview.ChampionshipsAdapter
import com.tal.android.pingpong.ui.mvp.view.EventsView
import com.tal.android.pingpong.ui.mvp.model.EventsModel
import org.greenrobot.eventbus.Subscribe

class EventsPresenter(view: EventsView, model: EventsModel) :
    BaseFragmentPresenter<EventsView, EventsModel>(view, model) {

    @Subscribe
    fun onChampionshipsFetchedSuccessfully(event: EventsModel.ChampionshipsFetchedSuccessfullyEvent) {
        view.setRefreshing(false)
        view.setChampionshipsAdapter(
            ChampionshipsAdapter(
                event.championships,
                model.getBus()
            )
        )
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

    @Subscribe
    fun onNewChampionshipButtonClicked(event: EventsView.NewChampionshipButtonClickedEvent) {
        startActivity(NewChampionshipActivity::class.java)
    }

    @Subscribe
    fun onChampionshipClicked(event: ChampionshipsAdapter.ChampionshipClickedEvent) {
        startActivity(
            ChampionshipActivity::class.java,
            bundleOf(ChampionshipActivity.CHAMPIONSHIP to event.championship)
        )
    }

    override fun onResume() {
        super.onResume()
        view.setRefreshing(true)
        model.fetchChampionships()
    }
}
