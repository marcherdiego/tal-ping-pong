package com.tal.android.pingpong.ui.mvp.presenter.matcheslist

import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.ui.adapters.recyclerview.UpcomingMatchesAdapter
import com.tal.android.pingpong.ui.mvp.model.matcheslist.BaseMatchesListModel
import com.tal.android.pingpong.ui.mvp.model.matcheslist.UpcomingMatchesModel
import com.tal.android.pingpong.ui.mvp.view.matcheslist.BaseMatchesListView
import org.greenrobot.eventbus.Subscribe

class UpcomingMatchesPresenter(view: BaseMatchesListView, model: UpcomingMatchesModel, bus: Bus) :
    BaseMatchesListPresenter<BaseMatchesListView, UpcomingMatchesModel>(view, model, bus) {

    @Subscribe
    fun onMatchesFetchedSuccessfully(event: BaseMatchesListModel.MatchesFetchedSuccessfullyEvent) {
        view.setRefreshing(false)
        view.setMatchesAdapter(
            UpcomingMatchesAdapter(
                event.matches,
                model.getUserEmail()
            )
        )
    }
}
