package com.tal.android.pingpong.ui.mvp.presenter.matcheslist

import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.ui.adapters.UnconfirmedMatchesAdapter
import com.tal.android.pingpong.ui.mvp.model.matcheslist.BaseMatchesListModel
import com.tal.android.pingpong.ui.mvp.model.matcheslist.UnconfirmedMatchesModel
import com.tal.android.pingpong.ui.mvp.view.matcheslist.BaseMatchesListView
import org.greenrobot.eventbus.Subscribe

class UnconfirmedMatchesPresenter(view: BaseMatchesListView, model: UnconfirmedMatchesModel, bus: Bus) :
    BaseMatchesListPresenter<BaseMatchesListView, UnconfirmedMatchesModel>(view, model, bus) {

    @Subscribe
    fun onMatchesFetchedSuccessfully(event: BaseMatchesListModel.MatchesFetchedSuccessfullyEvent) {
        view.setRefreshing(false)
        view.setMatchesAdapter(UnconfirmedMatchesAdapter(event.matches, model.getUserEmail(), Bus.defaultBus))
    }
}
