package com.tal.android.pingpong.ui.mvp.presenter.matcheslist

import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.ui.adapters.PastMatchesAdapter
import com.tal.android.pingpong.ui.mvp.model.matcheslist.BaseMatchesListModel
import com.tal.android.pingpong.ui.mvp.model.matcheslist.PastMatchesModel
import com.tal.android.pingpong.ui.mvp.view.matcheslist.BaseMatchesListView
import org.greenrobot.eventbus.Subscribe

class PastMatchesPresenter(view: BaseMatchesListView, model: PastMatchesModel, bus: Bus) :
    BaseMatchesListPresenter<BaseMatchesListView, PastMatchesModel>(view, model, bus) {

    @Subscribe
    fun onMatchesFetchedSuccessfully(event: BaseMatchesListModel.MatchesFetchedSuccessfullyEvent) {
        view.setRefreshing(false)
        view.setMatchesAdapter(PastMatchesAdapter(event.matches, model.getUserEmail()))
    }
}
