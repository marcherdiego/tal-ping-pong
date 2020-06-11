package com.tal.android.pingpong.ui.mvp.presenter.championship

import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.ui.adapters.recyclerview.PastMatchesAdapter
import com.tal.android.pingpong.ui.mvp.model.matcheslist.BaseMatchesListModel
import com.tal.android.pingpong.ui.mvp.model.championship.ReadOnlyMatchesModel
import com.tal.android.pingpong.ui.mvp.presenter.matcheslist.BaseMatchesListPresenter
import com.tal.android.pingpong.ui.mvp.view.matcheslist.BaseMatchesListView
import org.greenrobot.eventbus.Subscribe

class ReadOnlyMatchesPresenter(view: BaseMatchesListView, model: ReadOnlyMatchesModel, bus: Bus) :
    BaseMatchesListPresenter<BaseMatchesListView, ReadOnlyMatchesModel>(view, model, bus) {

    @Subscribe
    fun onMatchesFetchedSuccessfully(event: BaseMatchesListModel.MatchesFetchedSuccessfullyEvent) {
        view.setRefreshing(false)
        view.setMatchesAdapter(
            PastMatchesAdapter(
                event.matches,
                model.getUserEmail(),
                model.getBus()
            )
        )
    }
}
